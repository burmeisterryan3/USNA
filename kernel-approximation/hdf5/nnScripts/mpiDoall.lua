--[[
This scripts runs the scripts that do the meat of the work in
training and testing a neural network.  This file only returns
the result from the last epoch.
--]]
require 'torch'

function train_test()
  torch.setnumthreads(opt.threads)

  dofile 'model.lua'
  dofile 'loss.lua'
  dofile 'train.lua'
  dofile 'test.lua'

  local epoch = 1
  local results = torch.DoubleTensor(opt.nEpochs)
  local times = torch.DoubleTensor(opt.nEpochs)
  for i=1, opt.nEpochs do
    times[i] = train()
    results[i] = test() 
    epoch = epoch + 1
  end
  
  return results, times
end
