--[[
An incomplete file, but should allow for whitening of the data now.

The pca portion needs to include the mulitplication of the feature
matrix and w to reduce the number of dimensions in the data. We have
had memory troubles within the svd function call, so attempts to
limit the amount of memory allocated should be made. The dataset has
been reduced to a third of the size in this portion to account for
this.  However, the data split is on the same data for each running.
How big we can make this dataset for pca is unknown for a node on
shepard.

The cov portion has not yet been tested.

To reference these two methods, you can look at the github repository:
https://github.com/koraykv/unsup/blob/master/pca.lua#L15

To run the file, type "th convert.lua arg" where arg is either white,
pca, or cov based on the dataset you want to generate.
--]]

local mnist = require 'mnist'
local training = mnist.traindataset()
local testing = mnist.testdataset()
local allLabel = torch.cat(training.label, testing.label, 1) 
local training = training.data:double()
local testing = testing.data:double()
local allData = torch.cat(training, testing,1)
allData:resize(70000,784)

local convType = arg[1]
local filename = "/app/projects/usnadeep/data/mnist"

training = nil
testing = nil
collectgarbage()

if convType == 'white' then 
  local stddev = torch.std(allData, 1)
  for i=1,784 do
    if stddev[1][i] == 0 then
      stddev[1][i] = 1
    end
  end

  allData[{1,{}}] = allData[{1,{}}] - torch.mean(allData, 1)
  allData[{1,{}}] = torch.cdiv(allData[{1,{}}], stddev)

  data = {}
  for i=1,70000 do
    data[i] = {}
    data[i][1] = allData[i]
    data[i][2] = torch.Tensor(1)
    data[i][2][1] = allLabel[i]
  end
  filename = filename .. ".white" 
end

if convType == 'pca' then
  local size = allData:size(1)
  allData = allData - torch.ger(torch.ones(allData:size(1)),torch.mean(allData,1):squeeze())
  allData:div(math.sqrt(size-1))
  local w,s,v = torch.svd(allData:t())
  s:cmul(s)
  s = torch.gt(s,50)
  for i=1,s:size(1) do
    if s[i] == 0 then
      w = w:narrow(2, 1, i-1)
      break
    end
  end
  -- allData = allData X w
  filename = filename .. ".pca"
end

if convType == 'cov' then
  allData = allData:narrow(1,1,math.floor(70000/3))
  local size = allData:size(1)
  allData = allData - torch.ger(torch.ones(allData:size(1)),torch.mean(allData,1):squeeze())
  local c = torch.mm(allData:t(),allData)
  c:div(size-1)
  local ce,cv = torch.symeig(c,'V')
  -- print(ce,cv)
  filename = filename .. ".cov"
end

torch.save(filename, data)
