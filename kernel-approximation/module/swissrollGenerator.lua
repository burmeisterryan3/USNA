--[[
Generates the swissroll dataset.  To change the total amount of
data produced, change the numData variable and ptsPerSet variable, if
necessary.
--]]

ptsPerSet=80000 -- how many elements per class
filename='/app/projects/usnadeep/data/swissroll.data'

set1=torch.randn(ptsPerSet/2,2)+torch.Tensor(ptsPerSet/2,2):fill(7.5)
set1b=torch.randn(ptsPerSet/2,2)+torch.Tensor(ptsPerSet/2,2):fill(12.5)
set1=torch.cat(set1,set1b,1)
set2=torch.randn(ptsPerSet/2,2)+torch.cat(torch.Tensor(ptsPerSet/2,1):fill(7.5),torch.Tensor(ptsPerSet/2,1):fill(12.5),2)
set2b=torch.randn(ptsPerSet/2,2)+torch.cat(torch.Tensor(ptsPerSet/2,1):fill(12.5),torch.Tensor(ptsPerSet/2,1):fill(7.5),2)
set2=torch.cat(set2,set2b,1)
set=torch.cat(set1,set2,1)
output=torch.cat(torch.Tensor(ptsPerSet,1):fill(1),torch.Tensor(ptsPerSet,1):fill(2),1)
-- "set" is now the dataset, not yet rolled up
-- "output" is the target tensor

x=set:narrow(2,1,1)
y=set:narrow(2,2,1)
set = torch.cat( torch.cat( torch.cmul(x, torch.cos(x)), y, 2 ),
                 torch.cmul(x, torch.sin(x)), 2)

local numData = 160000
-- "set" is now nx3, and rolled up
mean = torch.mean(set, 1)*-1
mean = torch.repeatTensor(mean, numData, 1)
set = torch.add(set, mean)

dataset={}
function dataset:size() return numData end
for i=1,dataset:size() do
  dataset[i]={set[i]:reshape(3),torch.Tensor(1):fill(output[i][1])}
end
torch.save(filename,dataset)
