--[[
This file will run mpiDoall.lua to test various initialization of the
standard deviation for the distribution from which the weights are drawn.
The file uses a grid search to reach a sufficient standard deviation.
The current difference between two tests of the standard deviation for
which the program will stop is 0.01. This can be modified by altering the
loop condition.

The file takes multiple arguments listed below.  Some to take note of are
numClasses, nEpochs, nfeatures, nnodes, datapath, lbound, and ubound.

This file can be run with the runMNIST.sh where it is easy to specify
which hyperparameters you would like to test for optimization purposes.
-]]
require 'mpiT'

cmd = torch.CmdLine()
cmd:text()
cmd:text('SVHN Loss Function')
cmd:text()
cmd:text('Options:')
--cmd:option('-seed', 1, 'fixed input seed for repeatable experiments')
cmd:option('-threads', 2, 'number of threads')
-- data:
cmd:option('-split', '.75', 'percentage of the data that will be training')
cmd:option('-mnist', 'false', 'do you want to use torch\'s mnist dataset')
cmd:option('-datapath', '~/usna/torch/swissroll', 'filepath to the dataset')
-- model:
cmd:option('-model', 'cos', 'type of model to construct: linear | mlp | convnet | cos')
cmd:option('-stdDev', .25, 'standard deviation of the initialized parameters')
cmd:option('-nnodes', 10, 'number of nodes within the hidden layer')
cmd:option('-nfeatures', 3, 'number of nodes within visible layer')
cmd:option('-noutput', 2, 'number of nodes within output layer')
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
cmd:option('-lbound', 1, 'lower bound on covariance split')
cmd:option('-ubound', 10, 'upper bound on covariance split')
cmd:option('-binarize', 'false', 'do you want to pictures binarized')
cmd:text()

mpiT.Init()

_rank = torch.IntStorage(1)
mpiT.Comm_rank(mpiT.COMM_WORLD, _rank)
local rank = _rank[1]

_size = torch.IntStorage(1)
mpiT.Comm_size(mpiT.COMM_WORLD, _size)
local size = _size[1]

opt = cmd:parse(arg or {})

assert(size > 1)

local div = size/2
local root = 0
local difference = 1
local lbound = opt.lbound
local ubound = opt.ubound
local maxPer,bestStdDev;

while difference > 0.01 do   
  local allStdDev = torch.linspace(lbound, ubound, div)
  opt.stdDev = allStdDev[rank%div+1] -- Ranks start at 0, indices at 1
 
  dofile '../nnScripts/mpiDoall.lua'
  local rankPerc = torch.DoubleStorage(1)
  
  rankPerc[1] = train_test()

  local allPerc = torch.DoubleStorage(size)
  mpiT.Allgather(rankPerc,1,mpiT.DOUBLE,allPerc,1,mpiT.DOUBLE,mpiT.COMM_WORLD)

  local avgs = torch.Tensor(div)
  for i=1,div do
    avgs[i] = (allPerc[i]+allPerc[i+div])/2
  end
  maxPer,maxIndex = torch.max(avgs,1)
  bestStdDev = allStdDev[maxIndex[1]]
  
  local gridSearch = math.floor((ubound-lbound)/4)
  ubound = bestStdDev + gridSearch
  lbound = bestStdDev - gridSearch 
  if ubound > opt.ubound then
    ubound = opt.ubound
  elseif lbound < opt.lbound then
    lbound = opt.lbound
  end
  
  difference = (ubound-lbound)/(div-1)
end

if rank == 0 then
  print(bestStdDev, maxPer[1])
end

mpiT.Finalize()
