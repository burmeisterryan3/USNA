-- This file is supposed to be ran when you want to train/test a network
-- It is not used in MPI, only for debugging/trial purposes.
-- Logs each epochs accuracy. If you want the confusion matrix, you just change
-- train.lua and test.lua
require 'torch'

----------------------------------------------------------------------
--print '==> processing options'
cmd = torch.CmdLine()
cmd:text()
cmd:text('SVHN Loss Function')
cmd:text()
cmd:text('Options:')
-- global:
--cmd:option('-seed', 1, 'fixed input seed for repeatable experiments')
cmd:option('-threads', 2, 'number of threads')
-- data:
cmd:option('-split', '.75', 'percentage of the data that will be training')
cmd:option('-mnist', 'false', 'do you want to use torch\'s mnist dataset')
cmd:option('-datapath', '/gpfs/home/ben/usna/torch/swissroll', 'filepath to the dataset')
cmd:option('-binarize', 'false', 'do you want to pictures binarized')
-- model:
cmd:option('-model', 'cos', 'type of model to construct: linear | mlp | convnet | cos')
cmd:option('-stdDev', .25, 'standard deviation of the initialized parameters')
cmd:option('-nnodes', 10, 'number of nodes within the hidden layer')
cmd:option('-noutput', 2, 'number of nodes within output layer')
cmd:option('-nfeatures', 3, 'number of features per data point')
-- loss:
cmd:option('-loss', 'nll', 'type of loss function to minimize: nll | mse | margin')
-- training:
cmd:option('-save', 'results', 'subdirectory to save/log experiments in')
cmd:option('-plot', false, 'live plot')
cmd:option('-optimization', 'SGD', 'optimization method: SGD | ASGD | CG | LBFGS')
cmd:option('-learningRate', 1e-3, 'learning rate at t=0')
cmd:option('-batchSize', 1, 'mini-batch size (1 = pure stochastic)')
cmd:option('-weightDecay', 0, 'weight decay (SGD only)')
cmd:option('-momentum', 0, 'momentum (SGD only)')
cmd:option('-t0', 1, 'start averaging at t0 (ASGD only), in nb of epochs')
cmd:option('-maxIter', 2, 'maximum nb of iterations for CG and LBFGS')
cmd:option('-type', 'double', 'type: double | float | cuda')
cmd:option('-numClasses', 2, 'total number of classes')
cmd:option('-nEpochs', 10, 'total number of epochs')
cmd:text()
opt = cmd:parse(arg or {})

-- nb of threads and fixed seed (for repeatable experiments)
if opt.type == 'float' then
   torch.setdefaulttensortype('torch.FloatTensor')
elseif opt.type == 'cuda' then
   require 'cunn'
   torch.setdefaulttensortype('torch.FloatTensor')
end
torch.setnumthreads(opt.threads)


dofile 'data.lua'
dofile 'model.lua'
dofile 'loss.lua'
dofile 'train.lua'
dofile 'test.lua'


results = {}
resultSt = ""
for i=1, opt.nEpochs do
   train()
   test()
   resultSt = (resultSt .. ' ' .. results[i])
end

