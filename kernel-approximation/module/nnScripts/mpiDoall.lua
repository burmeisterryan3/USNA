--[[
This scripts runs the scripts that do the meat of the work in
training and testing a neural network.  This file only returns
the result from the last epoch.
--]]
require 'torch'

function train_test()
  torch.setnumthreads(opt.threads)

  dofile '../nnScripts/data.lua'
  dofile '../nnScripts/model.lua'
  dofile '../nnScripts/loss.lua'
  dofile '../nnScripts/train.lua'
  dofile '../nnScripts/test.lua'

  results = {}
  epoch = 1
  resultTensor=torch.DoubleTensor(opt.nEpochs)
  for i=1, opt.nEpochs do
     train()
     test()
     resultTensor[i]=results[i]
     epoch = epoch + 1
  end
  
  return resultTensor[opt.nEpochs]
end
