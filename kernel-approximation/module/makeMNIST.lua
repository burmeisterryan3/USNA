local mnist = require 'mnist'
local training = mnist.traindataset()
local testing = mnist.testdataset()
local tempTlab = training.label; local tempTelab = testing.label
local tempTrain = training.data:double()
local tempTest = testing.data:double()
local allData = torch.cat(tempTrain,tempTest,1)
local allLabel = torch.cat(tempTlab, tempTelab,1)

dataset={}
function dataset:size() return allData:size()[1] end
local data = torch.Tensor(allData:size()[1], 784)
for i=1, dataset:size() do
  dataset[i] = {allData[i]:reshape(784), torch.Tensor(1):fill(allLabel[i])}
end

torch.save('/app/projects/usnadeep/data/mnistdata', dataset)
