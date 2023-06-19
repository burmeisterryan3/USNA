----------------------------------------------------------------------
-- This script demonstrates how to define a training procedure,
-- irrespective of the model/loss functions chosen.
--
-- It shows how to:
--   + construct mini-batches on the fly
--   + define a closure to estimate (a noisy) loss
--     function, as well as its derivatives wrt the parameters of the
--     model to be trained
--   + optimize the function, according to several optmization
--     methods: SGD, L-BFGS.
--
-- Clement Farabet
----------------------------------------------------------------------

require 'torch'   -- torch
require 'xlua'    -- xlua provides useful tools, like progress bars
require 'optim'   -- an optimization package, for online and batch methods

----------------------------------------------------------------------
-- parse command line arguments
if not opt then
   print '==> processing options'
   cmd = torch.CmdLine()
   cmd:text()
   cmd:text('Training/Optimization')
   cmd:text()
   cmd:text('Options:')
   cmd:option('-save', 'results', 'subdirectory to save/log experiments in')
   cmd:option('-visualize', false, 'visualize input data and weights during training')
   cmd:option('-plot', false, 'live plot')
   cmd:option('-optimization', 'SGD', 'optimization method: SGD | ASGD | CG | LBFGS | Average')
   cmd:option('-loss', 'nll', 'type of loss function to minimize: nll | mse | margin')
   cmd:option('-model', 'convnet', 'type of model to construct: linear | mlp | convnet')
   cmd:option('-learningRate', 1e-3, 'learning rate at t=0')
   cmd:option('-batchSize', 1, 'mini-batch size (1 = pure stochastic)')
   cmd:option('-weightDecay', 0, 'weight decay (SGD only)')
   cmd:option('-miniEpochs', 1, 'Number of mini-epochs per epoch (Average SGD only)')
   cmd:option('-maxiEpochs', 2, 'Number of epochs after which mark is updated (SVRG only)') 
   cmd:option('-SGDiters', 1, 'Number of epochs of SGD (Average SGD and SVRG only)')
   cmd:option('-momentum', 0, 'momentum (SGD only)')
   cmd:option('-t0', 1, 'start averaging at t0 (ASGD only), in nb of epochs')
   cmd:option('-maxIter', 2, 'maximum nb of iterations for CG and LBFGS')
   cmd:option('-dataset', 'SVHN', 'dataset: SVHN | MNIST | CIFAR10')
   cmd:text()
   opt = cmd:parse(arg or {})
end

----------------------------------------------------------------------
-- CUDA?
if opt.type == 'cuda' then
   model:cuda()
   criterion:cuda()
end

----------------------------------------------------------------------
print '==> defining some tools'

-- classes
classes = {'1','2','3','4','5','6','7','8','9','0'}

-- This matrix records the current confusion across classes
confusion = optim.ConfusionMatrix(classes)

-- Log results to files
runID = opt.dataset .. '_' .. opt.optimization .. '_' .. opt.model .. '_' .. opt.loss .. '_lr' .. opt.learningRate .. '_batch' .. opt.batchSize .. '_wd' .. opt.weightDecay .. '_mom' .. opt.momentum
if opt.optimization == 'Average' then
   runID = runID .. '_miniE' .. opt.miniEpochs .. '_sgdI' .. opt.SGDiters
end
runID = runID .. '.log'
trainLogger = optim.Logger(paths.concat(opt.save, 'train_' .. runID))
trainLossLogger = optim.Logger(paths.concat(opt.save, 'trainLoss_' .. runID))
trainNormGLogger = optim.Logger(paths.concat(opt.save, 'trainNormG_' .. runID))
testLogger = optim.Logger(paths.concat(opt.save, 'test_' .. runID))

-- Retrieve parameters and gradients:
-- this extracts and flattens all the trainable parameters of the mode
-- into a 1-dim vector
if model then
   parameters,gradParameters = model:getParameters()
end

----------------------------------------------------------------------
print '==> configuring optimizer'

if opt.optimization == 'CG' then
   optimState = {
      maxIter = opt.maxIter
   }
   optimMethod = optim.cg

elseif opt.optimization == 'LBFGS' then
   optimState = {
      learningRate = opt.learningRate,
      maxIter = opt.maxIter,
      nCorrection = 10
   }
   optimMethod = optim.lbfgs

elseif opt.optimization == 'SGD' then
   optimState = {
      learningRate = opt.learningRate,
      weightDecay = opt.weightDecay,
      momentum = opt.momentum,
      learningRateDecay = 1e-7
   }
   optimMethod = optim.sgd

elseif opt.optimization == 'Average' then
   optimState = {
      learningRate = opt.learningRate,
      weightDecay = opt.weightDecay,
      momentum = opt.momentum,
      epochSize = trainData:size(),
      batchSize = opt.batchSize,
      miniEpochs = opt.miniEpochs,
      SGDiters = opt.SGDiters
   }
   optimMethod = average_sgd

elseif opt.optimization == 'ASGD' then
   optimState = {
      eta0 = opt.learningRate,
      t0 = trsize * opt.t0
   }
   optimMethod = optim.asgd

else
   error('unknown optimization method')
end

----------------------------------------------------------------------
print '==> defining training procedure'

function train()

   -- epoch tracker
   epoch = epoch or 1

   -- local vars
   local time = sys.clock()

   -- set model to training mode (for modules that differ in training and testing, like Dropout)
   model:training()

   -- shuffle at each epoch
   shuffle = torch.randperm(trsize)
   if optimMethod == optim.svrg then
      shuffle = torch.ceil(torch.rand(trsize)*trsize)
   end

   -- do one epoch
   print('==> doing epoch on training data:')
   print("==> online epoch # " .. epoch .. ' [batchSize = ' .. opt.batchSize .. ']')
   for t = 1,trainData:size(),opt.batchSize do
      -- disp progress
      xlua.progress(t, trainData:size())

      -- create mini batch
      local inputs = {}
      local targets = {}
      for i = t,math.min(t+opt.batchSize-1,trainData:size()) do
         -- load new sample
         local input = trainData.data[shuffle[i]]
         local target = trainData.labels[shuffle[i]]
         if opt.type == 'double' then input = input:double()
         elseif opt.type == 'cuda' then input = input:cuda() end
         table.insert(inputs, input)
         table.insert(targets, target)
      end

      -- create closure to evaluate f(X) and df/dX
      local feval = function(x, mark)
                       local mark = mark or 0
                       -- get new parameters
                       local temp_parameters = 0
                       if x ~= parameters and mark == 1 then
                          temp_parameters = parameters:clone()
                          parameters:copy(x)
                       elseif x ~= parameters and mark == 0 then
                          error('This condition probably should not be true!')
                          parameters:copy(x)
                          temp_parameters = parameters:clone()
                       else
                          temp_parameters = parameters:clone()
                       end

                       -- reset gradients
                       gradParameters:zero()

                       -- f is the average of all criterions
                       local f = 0

                       -- evaluate function for complete mini batch
                       for i = 1,#inputs do
                          -- estimate f
                          local output = model:forward(inputs[i])
                          local err = criterion:forward(output, targets[i])
                          f = f + err

                          -- estimate df/dW
                          local df_do = criterion:backward(output, targets[i])
                          model:backward(inputs[i], df_do)

                          -- update confusion
                          if mark == 0 then
                             confusion:add(output, targets[i])
                          end
                       end

                       -- normalize gradients and f(X)
                       gradParameters:div(#inputs)
                       f = f/#inputs

                       parameters:copy(temp_parameters)

                       -- return f and df/dX
                       return f,gradParameters
                    end

      -- optimize on current mini-batch
      if optimMethod == optim.asgd then
         _,_,average = optimMethod(feval, parameters, optimState)
      elseif optimMethod == optim.svrg then
         _,_,avg_x_temp = optimMethod(feval, parameters, optimState)
         markX = avg_x_temp:clone()
      else
         optimMethod(feval, parameters, optimState)
      end

      -- calculating one full gradient at the mark for SVRG
      if (t == trainData:size() or (t + opt.batchSize) > trainData:size()) and optimMethod == optim.svrg then
         if (epoch - optimState.SGDiters) % optimState.maxiEpochs == 0 and epoch >= optimState.SGDiters then
            -- save data in inputs
            inputs = {}
            targets = {}
            for i = 1,trainData:size() do
               -- load new sample
               local input = trainData.data[i]
               local target = trainData.labels[i]
               if opt.type == 'double' then input = input:double()
               elseif opt.type == 'cuda' then input = input:cuda() end
               table.insert(inputs, input)
               table.insert(targets, target)
            end

            -- calculate gradient of the function
            fMark, muMark_temp = feval(markX, 1)
            optimState.muMark = muMark_temp:clone()
         end
      end

      -- calculating training loss
      if (t == trainData:size() or (t + opt.batchSize) > trainData:size()) then
         -- save data in inputs
         inputs = {}
         targets = {}
         for i = 1,trainData:size() do
            -- load new sample
            local input = trainData.data[i]
            local target = trainData.labels[i]
            if opt.type == 'double' then input = input:double()
            elseif opt.type == 'cuda' then input = input:cuda() end
            table.insert(inputs, input)
            table.insert(targets, target)
         end
         fLoss, tempGrad = feval(parameters, 1)
         GradF = tempGrad:clone()
         NormG = torch.norm(GradF)
         print('Training Loss: ' .. fLoss)
         print('Norm of Gradient: ' .. NormG)
      end

   end

   -- time taken
   time = sys.clock() - time
   time = time / trainData:size()
   print("\n==> time to learn 1 sample = " .. (time*1000) .. 'ms')

   -- print confusion matrix
   print(confusion)

   -- update logger/plot
   trainLogger:add{['% mean class accuracy (train set)'] = confusion.totalValid * 100}
   if opt.plot then
      trainLogger:style{['% mean class accuracy (train set)'] = '-'}
      trainLogger:plot()
   end
   trainLossLogger:add{['training loss'] = fLoss}
   trainNormGLogger:add{['training gradient norm'] = NormG}

   -- save/log current net
   local filename = paths.concat(opt.save, 'model_' .. opt.model .. '.net')
   os.execute('mkdir -p ' .. sys.dirname(filename))
   print('==> saving model to '..filename)
   torch.save(filename, model)

   -- next epoch
   confusion:zero()
   epoch = epoch + 1
end
