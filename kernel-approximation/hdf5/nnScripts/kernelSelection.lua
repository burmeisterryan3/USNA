require 'torch'

cmd = torch.CmdLine()
cmd:text()
cmd:text('SVHN Loss Function')
cmd:text()
cmd:text('Options:')
cmd:option('-traindata', 'train.data', 'location to draw train data from')
cmd:option('-testdata', 'test.data', 'location to draw test data from')
cmd:option('-extradata', '', 'use extra data with trainset')
cmd:option('-threads', 2, 'number of threads')
cmd:option('-model', 'cos', 'type of model to construct: linear | mlp | convnet | cos')
cmd:option('-gridStdDev', false, 'perform gridsearch with bounds for standard deviation')
cmd:option('-gridNode', false, 'perform gridsearch with bounds for the number of nodes')
cmd:option('-gridLearnRate', false, 'perform gridsearch with bounds for the learning rate')
cmd:option('-gridWeightDecay', false, 'perform gridsearch with bounds for the weight decay')
cmd:option('-nfeatures', 3, 'number of nodes within visible layer')
cmd:option('-noutput', 2, 'number of nodes within output layer')
cmd:option('-loss', 'nll', 'type of loss function to minimize: nll | mse | margin')
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
cmd:option('-lowStd', 1, 'lower bound on covariance split')
cmd:option('-uprStd', 10, 'upper bound on covariance split')
cmd:option('-lowNodes', 10, 'lower bound on number of nodes in hidden layer')
cmd:option('-uprNodes', 10, 'upper bound on number of nodes in hidden layer')
cmd:option('-lowLearn', 1e-3, 'lower bound on the learning rate')
cmd:option('-uprLearn', 1e-3, 'upper bound on the learning rate')
cmd:option('-lowDecay', 0, 'lower bound on weight decay')
cmd:option('-uprDecay', 0, 'upper bound on weight decay')
cmd:option('-stdDev', 1, 'used when gridsearch is set to false')
cmd:option('-nnodes', 1, 'used when gridsearch is set to false')
cmd:option('-trsize', 1, 'percent of data to use in training, default utilizes all data')
cmd:text()

function updateStdDev(devDiff)
  if devDiff > .01 then
    local rcvStdDev = torch.DoubleStorage(1)
    -- Ensure that stdDevs are generated from the same random list
    if rank == root then
      allStdDev = torch.ones(size)*lowStd + torch.rand(size)*(uprStd-lowStd)
    end
    mpiT.Scatter(allStdDev:storage(),1,mpiT.DOUBLE,rcvStdDev,1,mpiT.DOUBLE,root,mpiT.COMM_WORLD)   
    return true, rcvStdDev[1]
  else
    if rank == root then
      allStdDev:fill(bestStdDev[1])
    end 
    return false, bestStdDev[1]
  end
end

function updateNodes(nodeDiff)
  if nodeDiff > 10 then
    local rcvNodes = torch.IntStorage(1)
    if rank == root then
      allNodes = (torch.ones(size)*lowNodes + torch.rand(size)*(uprNodes-lowNodes)):int()
    end
    mpiT.Scatter(allNodes:storage(),1,mpiT.INT,rcvNodes,1,mpiT.INT,root,mpiT.COMM_WORLD)
    return true, rcvNodes[1]
  else
    if rank == root then
      allNodes:fill(bestNumNodes[1])
    end
    return false, bestNumNodes[1]
  end
end

function updateLearn(learnDiff)
  if learnDiff > .00001 then
    local rcvLearn = torch.DoubleStroage(1)
    if rank == root then
      allLearn = torch.ones(size)*lowLearn + torch.rand(size)*(uprLearn-lowLearn)
    end
    mpiT.Scatter(allLearn:storage(),1,mpiT.DOUBLE,rcvLearn,1,mpiT.DOUBLE,root,mpiT.COMM_WORLD)
    return true, rcvLearn
  else
    if rank == root then
      allLearn:fill(bestLearnRate[1])
    end
    return false, bestLearnRate[1]
  end
end

function updateDecay(decayDiff)
  if decayDiff > .00001 then
    local rcvDecay = torch.DoubleStorage(1)
    if rank == root then
      allDecay = torch.ones(size)*lowDecay + torch.rand(size)*(uprDecay-lowDecay)
    end
    mpiT.Scatter(allDecay:storage(),1,mpiT.DOUBLE,rcvDecay,1,mpiT.DOUBLE,root,mpiT.COMM_WORLD)
    return true, rcvDecay[1]
  else
    if rank == root then
      allDecay:fill(bestDecayRate[1])
    end
    return false, bestDecayRate[1]
  end
end

function searchStdDev()
  local prevBestStdDev = bestStdDev[1]
  if rank == root then
    bestStdDev = torch.DoubleTensor(1):fill(allStdDev[maxIndex[1]])
  end
  mpiT.Bcast(bestStdDev:storage(),1,mpiT.DOUBLE,root,mpiT.COMM_WORLD)
  devDiff = math.abs(bestStdDev[1] - prevBestStdDev)
  if devDiff > .01 then
    local rangeDev = math.floor((uprStd-lowStd)/4)
    uprStd = bestStdDev[1] + rangeDev
    lowStd = bestStdDev[1] - rangeDev
    if uprStd > opt.uprStd then
      uprStd = opt.uprStd
    elseif lowStd < opt.lowStd then
      lowStd = opt.lowStd
    end
  end
  return lowStd, uprStd
end

function searchNodes()
  local prevBestNumNodes = bestNumNodes[1]
  if rank == root then  
    bestNumNodes = torch.IntTensor(1):fill(allNodes[maxIndex[1]])
  end
  mpiT.Bcast(bestNumNodes:storage(),1,mpiT.INT,root,mpiT.COMM_WORLD)
  nodeDiff = math.abs(bestNumNodes[1] - prevBestNumNodes)
  if nodeDiff > 10 then
    local rangeNodes = math.floor((uprNodes-lowNodes)/4)
    uprNodes = bestNumNodes[1] + rangeNodes
    lowNodes = bestNumNodes[1] - rangeNodes
    if uprNodes > opt.uprNodes then
      uprNodes = opt.uprNodes
    elseif lowNodes < opt.lowNodes then
      lowNodes = opt.lowNodes 
    end
  end
  return lowNodes, uprNodes
end

function searchLearnRate()
  local prevBestLearnRate = bestLearnRate[1]
  if rank == root then  
    bestLearnRate = torch.DoubleTensor(1):fill(allLearn[maxIndex[1]])
  end
  mpiT.Bcast(bestLearnRate:storage(),1,mpiT.DOUBLE,root,mpiT.COMM_WORLD)
  learnDiff = math.abs(bestLearnRate[1] - prevBestLearnRate)
  if learnDiff > .00001 then
    local rangeLearn = math.floor((uprLearn-lowLearn)/4)
    uprLearn = bestLearn[1] + rangeLearn
    lowLearn = bestLearn[1] - rangeLearn
    if uprLearn > opt.uprLearn then
      uprLearn = opt.uprLearn
    elseif lowLearn < opt.lowLearn then
      lowLearn = opt.lowLearn 
    end
  end
  return lowLearn, uprLearn
end

function searchWeightDecay()
  local prevBestWeightDecay = bestDecayRate[1]
  if rank == root then  
    bestDecayRate = torch.DoubleTensor(1):fill(allDecay[maxIndex[1]])
  end
  mpiT.Bcast(bestDecayRate:storage(),1,mpiT.DOUBLE,root,mpiT.COMM_WORLD)
  decayDiff = math.abs(bestDecayRate[1] - prevBestWeightDecay)
  if decayDiff > .00001 then
    local rangeDecay = math.floor((uprDecay-lowDecay)/4)
    uprDecay = bestDecayRate[1] + rangeDecay
    lowDecay = bestDecayRate[1] - rangeDecay
    if uprDecay > opt.uprDecay then
      uprDecay = opt.uprDecay
    elseif lowDecay < opt.lowDecay then
      lowDecay = opt.lowDecay
    end
  end
  return lowDecay, uprDecay
end

function printStmt()
  local stmt = ''
  if opt.gridStdDev then
    stmt = string.format("%.4f",bestStdDev[1])
  else
    stmt = opt.stdDev
  end
  if opt.gridNode then
    stmt = stmt .. ' ' .. bestNumNodes[1]
  else
    stmt = stmt .. ' ' .. opt.nnodes
  end
  if opt.gridLearningRate then
    stmt = stmt .. ' ' .. string.format("%.4f",bestLearnRate[1]) 
  else
    stmt = stmt .. ' ' .. opt.learningRate
  end
  if opt.gridWeightDecay then
    stmt = stmt .. ' ' .. string.format("%.4f",bestDecayRate[1])
  else
    stmt = stmt .. ' ' .. opt.weightDecay
  end
  for i=1,opt.nEpochs do
    stmt = stmt .. ' ' .. rankPerc[i] .. ' ' .. times[i]
  end
  print(stmt)
end

opt = cmd:parse(arg or {})

require 'mpiT'
mpiT.Init()

_rank = torch.IntStorage(1)
mpiT.Comm_rank(mpiT.COMM_WORLD, _rank)
rank = _rank[1]

_size = torch.IntStorage(1)
mpiT.Comm_size(mpiT.COMM_WORLD, _size)
size = _size[1]

assert(size > 1)
dofile 'data.lua'
root = 0
maxIndex = torch.LongTensor(1)
local maxPer=-1*math.huge
runDev,runNode,runLearn,runDecay = false,false,false,false

if opt.gridStdDev then
  allStdDev = torch.Tensor(size)
  devDiff = math.huge
  bestStdDev=torch.DoubleTensor(1):fill(-100)
  lowStd = opt.lowStd
  uprStd = opt.uprStd
  runDev = true
end
if opt.gridNode then
  allNodes = torch.Tensor(size)
  nodeDiff = math.huge
  bestNumNodes=torch.IntTensor(1):fill(-100)
  lowNodes = opt.lowNodes
  uprNodes = opt.uprNodes
  runNode = true
end
if opt.gridLearningRate then
  allLearn = torch.Tensor(size)
  learnDiff = math.huge
  bestLearnRate = torch.DoubleTensor(1):fill(-100)
  lowLearn = opt.lowLearn
  uprLearn = opt.uprLearn
  runLearn = true
end
if opt.gridWeightDecay then
  allDecay = torch.Tensor(size)
  decayDiff = math.huge
  bestDecayRate = torch.DoubleTensor(1):fill(-100)
  lowDecay = opt.lowDecay
  uprDecay = opt.uprDecay
  runDecay = true
end

if opt.model == 'cos' then
  while true do
    if opt.gridStdDev then
      runDev, stdDev = updateStdDev(devDiff)
    else
      stdDev = opt.stdDev
    end
    if opt.gridNode then
      runNode,nnodes = updateNodes(nodeDiff)
    else
      nnodes = opt.nnodes
    end
    if opt.gridLearningRate then
      runLearn, learnRate = updateLearn(learnDiff) 
    else
      learnRate = opt.learningRate
    end
    if opt.gridWeightDecay then
      runDecay, weightDecay = updateDecay(decayDiff)
    else
      weightDecay = opt.weightDecay
    end
  
    if not runNode and not runDev and not runLearn and not runDecay then
      break
    end 
 
    dofile 'mpiDoall.lua'

    rankPerc = torch.DoubleStorage(opt.nEpochs) 
    rankPerc,times = train_test()

    local sendPerc = torch.DoubleStorage(1):fill(rankPerc[opt.nEpochs])
    local allPerc = torch.DoubleStorage(size)
    mpiT.Allgather(sendPerc,1,mpiT.DOUBLE,allPerc,1,mpiT.DOUBLE,mpiT.COMM_WORLD)

    if rank == root then
      maxPer,maxIndex = torch.max(torch.Tensor(allPerc),1)
    end
    
    mpiT.Bcast(maxIndex:storage(),1,mpiT.LONG,root,mpiT.COMM_WORLD)
    if opt.gridStdDev then
      lowStd,uprStd = searchStdDev()
    end
    if opt.gridNode then
      lowNodes,uprNodes = searchNodes()
    end 
    if opt.gridLearningRate then
      lowLearn,uprLearn = searchLearnRate() 
    end
    if opt.gridWeightDecay then
      lowDecay,uprDecay = searchWeightDecay()
    end
    
    -- Print the results from the run
    if maxIndex[1] == rank+1 then
      printStmt()
    end
  end
else
  while true do
    if opt.gridNode then
      runNode, nnodes = updateNodes(nodeDiff)
    else
      nnodes = opt.nnodes
    end
    if opt.gridLearningRate then
      runLearn, learnRate = updateLearn(learnDiff) 
    else
      learnRate = opt.learningRate
    end
    if opt.gridWeightDecay then
      runDecay, weightDecay = updateDecay(decayDiff)
    else
      weightDecay = opt.weightDecay
    end 

    if not runNode and not runLearn and not runDecay then
      break
    end 
    
    dofile 'mpiDoall.lua'

    rankPerc = torch.DoubleStorage(opt.nEpochs) 
    rankPerc,times = train_test()

    local sendPerc = torch.DoubleStorage(1):fill(rankPerc[opt.nEpochs])
    local allPerc = torch.DoubleStorage(size)
    mpiT.Allgather(sendPerc,1,mpiT.DOUBLE,allPerc,1,mpiT.DOUBLE,mpiT.COMM_WORLD)

    if rank == root then
      maxPer,maxIndex = torch.max(torch.Tensor(allPerc),1)
    end

    mpiT.Bcast(maxIndex:storage(),1,mpiT.LONG,root,mpiT.COMM_WORLD)
    if opt.gridNode then
      lowNodes, uprNodes = searchNodes()
    end
    if opt.gridLearningRate then
      lowLearn,uprLearn = searchLearnRate() 
    end
    if opt.gridWeightDecay then
      lowDecay,uprDecay = searchWeightDecay()
    end
    
    -- Print the results from the run
    if maxIndex[1] == rank+1 then
      printStmt()
    end
  end
end

mpiT.Finalize()
