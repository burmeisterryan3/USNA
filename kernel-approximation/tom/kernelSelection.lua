require 'mpiT'

cmd = torch.CmdLine()
cmd:text('HouseNumber Training')
cmd:text()
cmd:text('Options:')
--cmd:option('-save', fname:gsub('.lua',''), 'subdirectory to save/log experiments in')
cmd:option('-network', '', 'reload pretrained network')
cmd:option('-extra', false, 'use extra training samples dataset (~500,000 extra training samples)')
cmd:option('-visualize', false, 'visualize input data and weights during training')
cmd:option('-seed', 1, 'fixed input seed for repeatable experiments')
cmd:option('-plot', true, 'live plot')
cmd:option('-optimization', 'SGD', 'optimization method: SGD | ASGD | CG | LBFGS')
cmd:option('-learningRate', 1e-3, 'learning rate at t=0')
cmd:option('-batchSize', 1, 'mini-batch size (1 = pure stochastic)')
cmd:option('-weightDecay', 0, 'weight decay (SGD only)')
cmd:option('-momentum', 0, 'momentum (SGD only)')
cmd:option('-t0', 1, 'start averaging at t0 (ASGD only), in nb of epochs')
cmd:option('-maxIter', 2, 'maximum nb of iterations for CG and LBFGS')
cmd:option('-threads', 2, 'nb of threads to use')
cmd:option('-useKernel', false, 'use cos kernel')
cmd:option('-kernelStd', 0, 'kernel weight initialization stdDev')
cmd:option('-lbound',0.0001,'lower bound for grid search')
cmd:option('-ubound',10,'upper bound for grid search')
cmd:text()
opt = cmd:parse(arg)

mpiT.Init()

_rank = torch.IntStorage(1)
mpiT.Comm_rank(mpiT.COMM_WORLD, _rank)
local rank = _rank[1]

_size = torch.IntStorage(1)
mpiT.Comm_size(mpiT.COMM_WORLD, _size)
local size = _size[1]

assert(size > 1)

local div = size/2
local allStdDev = torch.zeros(div)
local rankStdDev = torch.DoubleStorage(1)
local root = 0
local difference = 1
local lbound = opt.lbound
local ubound = opt.ubound
local maxPer,bestStdDev;

while difference > 0.01 do  
  difference = (ubound-lbound)/(div-1)
  
  allStdDev = torch.linspace(lbound, ubound, div)
  allStdDev = allStdDev:repeatTensor(1, 2)
  opt.kernelStd = allStdDev[1][rank+1]
 
  dofile 'train_housenumbers_func.lua'
  local rankPerc = torch.DoubleStorage(1)
  rankPerc[1] = runIt(opt)

  local allPerc = torch.DoubleStorage(size)
  mpiT.Allgather(rankPerc,1,mpiT.DOUBLE,allPerc,1,mpiT.DOUBLE,mpiT.COMM_WORLD)

  local maxIndex;
  local avgs = torch.Tensor(div)
  for i=1,div do
    avgs[i] = (allPerc[i]+allPerc[i+div])/2
  end
  maxPer,maxIndex = torch.max(avgs,1)
  bestStdDev = allStdDev[1][maxIndex[1]]
  
  local newtonSplit = math.floor((ubound-lbound)/4)
  ubound = bestStdDev + newtonSplit
  lbound = bestStdDev - newtonSplit 
  if ubound > opt.ubound then
    ubound = opt.ubound
  elseif lbound < opt.lbound then
    lbound = opt.lbound
  end
end

if rank == 0 then
  print(bestStdDev, maxPer)
end

mpiT.Finalize()
