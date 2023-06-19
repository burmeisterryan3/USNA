matio = require 'matio'

-- download data 
train_dl = 'http://ufldl.stanford.edu/housenumbers/train_32x32.mat'
test_dl = 'http://ufldl.stanford.edu/housenumbers/test_32x32.mat'
ok = io.open('train_32x32.mat', 'r'); ok1 = io.open('test_32x32.mat', 'r')
if ok~=nil then print('Training data downloaded'); io.close(ok) 
else os.execute('wget ' .. train_dl) end
if ok1~=nil then print('Testing data downloaded'); io.close(ok1) 
else os.execute('wget ' .. test_dl) end

-- load data
training = matio.load('train_32x32.mat')
testing = matio.load('test_32x32.mat')
training.X = training.X:double(); testing.X = testing.X:double()

-- initialize storage structs
N = training.y:size(1) + testing.y:size(1)
grey_pictures = torch.Tensor(32,32,N)
picture_mean = torch.Tensor(32,32):zero()
picture_stdDev = torch.Tensor(32,32):zero()

-- average the color values
for i=1, N do
	if i > training.y:size(1) then j=i-training.y:size(1); grey_pictures[{{},{},i}] = torch.add(testing.X[{{},{},1,j}], testing.X[{{},{},2,j}]):add(testing.X[{{},{},3,j}]):div(3):clone() 
	else grey_pictures[{{},{},i}] = torch.add(training.X[{{},{},1,i}], training.X[{{},{},2,i}]):add(training.X[{{},{},3,i}]):div(3):clone() end
	picture_mean = picture_mean + grey_pictures[{{},{},i}]; 
end

-- calculate the std dev and mean
picture_mean = picture_mean:div(N)
for i=1, N do
	local diff = grey_pictures[{{},{},i}] - picture_mean
	local stdDev = torch.cmul(diff, diff)
	picture_stdDev = picture_stdDev + stdDev
end
picture_stdDev = torch.div(picture_stdDev, N)

-- subtract the mean and divide by stdDev/100 for each picture's pixel
processed_pictures = torch.Tensor(32,32,N)
for i=1,N do
	local pic = grey_pictures[{{},{},i}]:clone()
	pic = pic - picture_mean
	pic = pic:cdiv(picture_stdDev):div(100)
	processed_pictures[{{},{},i}] = pic
end

-- save to file
filename='processed_street'
dataset = {}
dataset.X = processed_pictures:double()
dataset.y = training.y:double()
function dataset:size() return 99289 end 
torch.save(filename, dataset)
