--[[
  number of Features is 648
  number of Classes is 10
--]]

require 'hdf5'

filepath = "/app/projects/usnadeep/data/housenumbers/SVHN_train_hog8.h5"
local file = hdf5.open(filepath, 'r')
local traindata = file:read("/X"):all()
local trainlabels = file:read("/Y"):all()
file:close()

trainlabels = trainlabels:double()
local dataset = {}
function dataset:size() return (#traindata)[1] end
for i=1, dataset:size() do
  dataset[i] = {traindata[i], trainlabels[i]}
end

torch.save("/app/projects/usnadeep/data/housenumbers/SVHN_train_hog8.data", dataset)

filepath = "/app/projects/usnadeep/data/housenumbers/SVHN_test_hog8.h5"

file = hdf5.open(filepath, 'r')
local testdata = file:read("/X"):all()
local testlabels = file:read("/Y"):all()
file:close()

testlabels = testlabels:double()
dataset = {}
function dataset:size() return (#testdata)[1] end
for i=1, dataset:size() do
  dataset[i] = {testdata[i], testlabels[i]}
end

torch.save("/app/projects/usnadeep/data/housenumbers/SVHN_test_hog8.data", dataset)

filepath = "/app/projects/usnadeep/data/housenumbers/SVHN_extra_hog8.h5"

file = hdf5.open(filepath, 'r')
local testdata = file:read("/X"):all()
local testlabels = file:read("/Y"):all()
file:close()

testlabels = testlabels:double()
dataset = {}
function dataset:size() return (#testdata)[1] end
for i=1, dataset:size() do
  dataset[i] = {testdata[i], testlabels[i]}
end

torch.save("/app/projects/usnadeep/data/housenumbers/SVHN_extra_hog8.data", dataset)
