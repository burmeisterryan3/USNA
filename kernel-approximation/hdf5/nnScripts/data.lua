-- This file loads and splits data according to the torch dataset format. 
-- If this file is ran standalone, command line argument must be passed in.
-- Otherwise a table opt must specifcy the options for this file.
require 'torch'   -- torch
require 'image'   -- for color transforms
require 'nn'      -- provides a normalization operator


-- randomly splits the swissroll dataset
function buildData(train, test)
  numTrain = (#train)
  numTest = (#test)
  local x_train = torch.Tensor(numTrain, train[1][1]:size(1)):double()
  local y_train = torch.Tensor(numTrain)
  local x_test = torch.Tensor(numTest, test[1][1]:size(1))
  local y_test = torch.Tensor(numTest)
  --randomly shuffle data into train and test
  for i=1, numTrain do
    x_train[i] = train[i][1]:clone()
    y_train[i] = train[i][2]:clone()
  end 
  for i=1, numTest do
    x_test[i] = test[i][1]:clone()
    y_test[i] = test[i][2]:clone()
  end 
  
  --combine the data into a table for nn:forward()
  local testComb = {}
  local trainComb = {}
  for i=1,numTrain do
    trainComb[i] = {x_train[i], y_train[i]}
  end 
  for i=1,numTest do
    testComb[i] = {x_test[i], y_test[i]}
  end 
  --standardize the structs of the data
  trainData = {
    data = x_train,
    labels = y_train,
    combined = trainComb,
    size = function() return x_train:size(1) end,
  }
  testData ={
    data = x_test,
    labels = y_test,
    combined = testComb,
    size = function() return x_test:size(1) end,
  }
end

-- if called will binarize a picture based off of the threshold value
function binarize(train, test, threshold)
	for i=1, train:size(1) do
		train.data[i][train.data[i]:lt(threshold)] = 0;
		train.data[i][train.data[i]:ge(threshold)] = 1;
	end
	for i=1, test:size(1) do
		test.data[i][test.data[i]:lt(threshold)] = 0;
		test.data[i][test.data[i]:ge(threshold)] = 1;
	end
	return train, test
end

local train = torch.load(opt.traindata)
local extra = {}
if opt.extradata ~= '' then
  extra = torch.load(opt.extradata)
  local train_len = #train
  local total_len = train_len + #extra
  local ii = 0
  for i = train_len+1, total_len do
    ii = ii + 1
    train[i] = extra[ii]
  end
end
local test = torch.load(opt.testdata)

buildData(train, test)
