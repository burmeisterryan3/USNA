require 'torch'
require 'nn'
require 'nnx'
require 'optim'
require 'image'
require 'Cos'
require 'LinearGaussian'

function load_housenumber_data(opt)
   ----------------------------------------------------------------------
   -- get/create dataset
   --

   www = 'http://torch7.s3-website-us-east-1.amazonaws.com/data/housenumbers/'
   data_home = '/app/projects/usnadeep/data/housenumbers/'
   train_file = 'train_32x32.t7'
   test_file = 'test_32x32.t7'
   extra_file = 'extra_32x32.t7'
   housenumbers_preprocessed = 'housenumbers_32x32_preprocessed.t7'
   extra_housenumbers_preprocessed = 'extra_housenumbers_32x32_preprocessed.t7'
   -- Check to see if the data has already been processed

   if opt.extra then
      trsize = 73257 + 531131
      tesize = 26032
      print('HERE')
   else
      --print '<trainer> WARNING: using reduced train set'
      --print '(use -extra to use complete training set, with extra samples)'
      trsize = 73257
      tesize = 26032
   end

   print('Searching for pre-processed data files...')
   if opt.extra and paths.filep(data_home..extra_housenumbers_preprocessed ) then
      print('Loading extra large dataset from preprocessed file...')
      data = torch.load(data_home..extra_housenumbers_preprocessed)
      return data.trainData, data.testData, trsize, tesize
   end

   
   if not opt.extra and paths.filep(data_home..housenumbers_preprocessed ) then
      print('Loading housenumbers dataset from preprocessed file...')
      data = torch.load(data_home..housenumbers_preprocessed)
      return data.trainData, data.testData, trsize, tesize
   end
 
   print('No preprocessed data found.  Retrieving dataset...')

   if not paths.filep(data_home..train_file) then
      os.execute('wget ' .. www .. train_file)
   end
   if not paths.filep(data_home..test_file) then
      os.execute('wget ' .. www .. test_file)
   end
   if opt.extra and not paths.filep(data_home..extra_file) then
      os.execute('wget ' .. www .. extra_file)   
   end

   loaded = torch.load(data_home..train_file,'ascii')
   trainData = {
      data = loaded.X:transpose(3,4),
      labels = loaded.y[1],
      size = function(self) return self.data:size(1) end
   }

   if opt.extra then
      loaded = torch.load(data_home..extra_file,'ascii')
      trdata = torch.Tensor(trsize,3,32,32)
      trdata[{ {1,(#trainData.data)[1]} }] = trainData.data
      trdata[{ {(#trainData.data)[1]+1,-1} }] = loaded.X:transpose(3,4)
      trlabels = torch.Tensor(trsize)
      trlabels[{ {1,(#trainData.labels)[1]} }] = trainData.labels
      trlabels[{ {(#trainData.labels)[1]+1,-1} }] = loaded.y[1]
      trainData = {
         data = trdata,
         labels = trlabels,
         size = function(self) return self.data:size(1) end
      }
   end

   loaded = torch.load(data_home..test_file,'ascii')
   testData = {
      data = loaded.X:transpose(3,4),
      labels = loaded.y[1],
      size = function(self) return self.data:size(1) end
   }

   ----------------------------------------------------------------------
   -- preprocess/normalize train/test sets
   --

   --print '<trainer> preprocessing data (color space + normalization)'

   -- preprocess requires floating point
   trainData.data = trainData.data:float()
   testData.data = testData.data:float()

   -- preprocess trainSet
   normalization = nn.SpatialContrastiveNormalization(1, image.gaussian1D(7)):float()
   for i = 1,trainData:size() do
      -- rgb -> yuv
      local rgb = trainData.data[i]
      local yuv = image.rgb2yuv(rgb)
      -- normalize y locally:
      yuv[1] = normalization(yuv[{{1}}])
      trainData.data[i] = yuv
   end
   -- normalize u globally:
   mean_u = trainData.data[{ {},2,{},{} }]:mean()
   std_u = trainData.data[{ {},2,{},{} }]:std()
   trainData.data[{ {},2,{},{} }]:add(-mean_u)
   trainData.data[{ {},2,{},{} }]:div(-std_u)
   -- normalize v globally:
   mean_v = trainData.data[{ {},3,{},{} }]:mean()
   std_v = trainData.data[{ {},3,{},{} }]:std()
   trainData.data[{ {},3,{},{} }]:add(-mean_v)
   trainData.data[{ {},3,{},{} }]:div(-std_v)

   -- preprocess testSet
   for i = 1,testData:size() do
      -- rgb -> yuv
      local rgb = testData.data[i]
      local yuv = image.rgb2yuv(rgb)
      -- normalize y locally:
      yuv[{1}] = normalization(yuv[{{1}}])
      testData.data[i] = yuv
   end
   -- normalize u globally:
   testData.data[{ {},2,{},{} }]:add(-mean_u)
   testData.data[{ {},2,{},{} }]:div(-std_u)
   -- normalize v globally:
   testData.data[{ {},3,{},{} }]:add(-mean_v)
   testData.data[{ {},3,{},{} }]:div(-std_v)

   if opt.extra then
      --print('Saving extra large dataset to preprocessed file...')
      data = torch.save(data_home..extra_housenumbers_preprocessed,{trainData=trainData,testData=testData})
   end
   if not opt.extra then
      --print('Saving housenumber dataset to preprocessed file...')
      data = torch.save(data_home..housenumbers_preprocessed,{trainData=trainData,testData=testData})
   end

   return trainData,testData,trsize,tesize
end

-- training function
function train(dataset,trsize)

   ----------------------------------------------------------------------
   -- define training and testing functions
   --

   -- this matrix records the current confusion across classes
   confusion = optim.ConfusionMatrix(classes)

   -- log results to files
   --trainLogger = optim.Logger(paths.concat(opt.save, 'train.log'))
   --testLogger = optim.Logger(paths.concat(opt.save, 'test.log'))

   -- display
   if opt.visualize then
      require 'image'
      local trset = trainData.data[{ {1,100} }]
      local teset = testData.data[{ {1,100} }]
      image.display{image=trset, legend='training set', nrow=10, padding=1}
      image.display{image=teset, legend='test set', nrow=10, padding=1}
   end



   -- epoch tracker
   epoch = epoch or 1

   -- local vars
   local time = sys.clock()

   -- shuffle at each epoch
   shuffle = torch.randperm(trsize)

   -- do one epoch
   --print('<trainer> on training set:')
   --print("<trainer> online epoch # " .. epoch .. ' [batchSize = ' .. opt.batchSize .. ']')
   for t = 1,dataset:size(),opt.batchSize do
      -- disp progress
      --xlua.progress(t, dataset:size())

      -- create mini batch
      local inputs = {}
      local targets = {}
      for i = t,math.min(t+opt.batchSize-1,dataset:size()) do
         -- load new sample
         local input = dataset.data[shuffle[i]]:double()
         local target = dataset.labels[shuffle[i]]
         table.insert(inputs, input)
         table.insert(targets, target)
      end

      -- create closure to evaluate f(X) and df/dX
      local feval = function(x)
                       -- get new parameters
                       if x ~= parameters then
                          parameters:copy(x)
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
                          confusion:add(output, targets[i])
                       end

                       -- normalize gradients and f(X)
                       gradParameters:div(#inputs)
                       f = f/#inputs

                       -- return f and df/dX
                       return f,gradParameters
                    end

      -- optimize on current mini-batch
      if opt.optimization == 'CG' then
         config = config or {maxIter = opt.maxIter}
         optim.cg(feval, parameters, config)

      elseif opt.optimization == 'LBFGS' then
         config = config or {learningRate = opt.learningRate,
                             maxIter = opt.maxIter,
                             nCorrection = 10}
         optim.lbfgs(feval, parameters, config)

      elseif opt.optimization == 'SGD' then
         config = config or {learningRate = opt.learningRate,
                             weightDecay = opt.weightDecay,
                             momentum = opt.momentum,
                             learningRateDecay = 5e-7}
         optim.sgd(feval, parameters, config)

      elseif opt.optimization == 'ASGD' then
         config = config or {eta0 = opt.learningRate,
                             t0 = trsize * opt.t0}
         _,_,average = optim.asgd(feval, parameters, config)

      else
         error('unknown optimization method')
      end
   end

   -- time taken
   time = sys.clock() - time
   time = time / dataset:size()
   --print("<trainer> time to learn 1 sample = " .. (time*1000) .. 'ms')

   -- print confusion matrix
   print('TRAINING CONFUSION')
   print(confusion)
   --trainLogger:add{['% mean class accuracy (train set)'] = confusion.totalValid * 100}
   confusion:zero()

   -- save/log current net
   --local filename = paths.concat(opt.save, 'house.net')
   --os.execute('mkdir -p ' .. sys.dirname(filename))
   --print('<trainer> saving network to '..filename)
   --torch.save(filename, model)

   -- next epoch
   epoch = epoch + 1
end

-- test function
function test(dataset)
   -- local vars
   local time = sys.clock()

   -- averaged param use?
   if average then
      cachedparams = parameters:clone()
      parameters:copy(average)
   end

   -- test over given dataset
   --print('<trainer> on testing Set:')
   for t = 1,dataset:size() do
      -- disp progress
      --xlua.progress(t, dataset:size())

      -- get new sample
      local input = dataset.data[t]:double()
      local target = dataset.labels[t]

      -- test sample
      local pred = model:forward(input)
      confusion:add(pred, target)
   end

   -- timing
   time = sys.clock() - time
   time = time / dataset:size()
   --print("<trainer> time to test 1 sample = " .. (time*1000) .. 'ms')

   -- print confusion matrix
   print('TESTING CONFUSION')
   print(confusion)
   --testLogger:add{['% mean class accuracy (test set)'] = confusion.totalValid * 100}
   confusion:updateValids()
   local accuracy=confusion.totalValid*100
   confusion:zero()

   -- averaged param use?
   if average then
      -- restore parameters
      parameters:copy(cachedparams)
   end
   return accuracy
end

function runIt(opt)
   -- fix seed
   torch.manualSeed(opt.seed)

   -- threads
   torch.setnumthreads(opt.threads)
   --print('<torch> set nb of threads to ' .. opt.threads)

   ----------------------------------------------------------------------
   -- define model to train
   -- on the 10-class classification problem
   --
   classes = {'1','2','3','4','5','6','7','8','9','0'}

   if opt.network == '' then
      ------------------------------------------------------------
      -- convolutional network 
      -- this is a typical convolutional network for vision:
      --   1/ the image is transformed into Y-UV space
      --   2/ the Y (luminance) channel is locally normalized,
      --      while the U/V channels are more loosely normalized
      --   3/ the first two stages features are locally pooled
      --      using LP-pooling (P=2)
      --   4/ a two-layer neural network is applied on the
      --      representation
      ------------------------------------------------------------
      if opt.useKernel then
         -- top container
         model = nn.Sequential()
         -- stage 1 : filter bank -> squashing -> max pooling
         model:add(nn.SpatialConvolutionMap(nn.tables.random(3,16,1), 5, 5))
         model:add(nn.Tanh())
         model:add(nn.SpatialLPPooling(16,2,2,2,2,2))
         -- stage 2 : filter bank -> squashing -> max pooling
         model:add(nn.SpatialSubtractiveNormalization(16, image.gaussian1D(7)))
         model:add(nn.SpatialConvolutionMap(nn.tables.random(16, 256, 4), 5, 5))
         model:add(nn.Cos())
         model:add(nn.SpatialLPPooling(256,2,2,2,2,2))
         -- stage 3 : standard 2-layer neural network
         model:add(nn.SpatialSubtractiveNormalization(256, image.gaussian1D(7)))
         model:add(nn.Reshape(256*5*5))
         if opt.kernelStd ~= 0 then
   	    model:add(nn.LinearGaussian(256*5*5, 128, opt.kernelStd))
         else
   	    model:add(nn.LinearGaussian(256*5*5, 128))
         end
         model:add(nn.Cos())   -- We get about 1.5-2% better when ONLY this unit is a cosine
         model:add(nn.Linear(128,#classes))
         model:add(nn.LogSoftMax())
      else 
         -- top container
         model = nn.Sequential()
         -- stage 1 : filter bank -> squashing -> max pooling
         model:add(nn.SpatialConvolutionMap(nn.tables.random(3,16,1), 5, 5))
         model:add(nn.Tanh())
         model:add(nn.SpatialLPPooling(16,2,2,2,2,2))
         -- stage 2 : filter bank -> squashing -> max pooling
         model:add(nn.SpatialSubtractiveNormalization(16, image.gaussian1D(7)))
         model:add(nn.SpatialConvolutionMap(nn.tables.random(16, 256, 4), 5, 5))
         model:add(nn.Tanh())
         model:add(nn.SpatialLPPooling(256,2,2,2,2,2))
         -- stage 3 : standard 2-layer neural network
         model:add(nn.SpatialSubtractiveNormalization(256, image.gaussian1D(7)))
         model:add(nn.Reshape(256*5*5))
         model:add(nn.LinearGaussian(256*5*5, 128))
         model:add(nn.Tanh())
         model:add(nn.Linear(128,#classes))
         model:add(nn.LogSoftMax())
      end
      
      ------------------------------------------------------------
   else
      --print('<trainer> reloading previously trained network')
      model = torch.load(opt.network)
   end

   -- retrieve parameters and gradients
   parameters,gradParameters = model:getParameters()

   -- verbose
   --print('<trainer> using model:')
   --print(model)

   ----------------------------------------------------------------------
   -- loss function: negative log-likelihood
   --
   criterion = nn.ClassNLLCriterion()

   trainData,testData,trsize,tesize = load_housenumber_data(opt)
   ----------------------------------------------------------------------
   -- and train!
   --
   iter = 0
   local accuracy=0
   while iter < opt.maxIter do
      iter = iter+1
      -- train/test
      train(trainData,trsize)
      accuracy=test(testData)

      -- plot errors
      --trainLogger:style{['% mean class accuracy (train set)'] = '-'}
      --testLogger:style{['% mean class accuracy (test set)'] = '-'}
      --if opt.plot then
         --trainLogger:plot()
         --testLogger:plot()
      --end
      --print(accuracy)
   end
   return accuracy
end
