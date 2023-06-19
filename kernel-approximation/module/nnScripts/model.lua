-- This file builds the networks model, only have used with the options linear and cosine
-- If this file is ran standalone, command line argument must be passed in.
-- Otherwise a table opt must specifcy the options for this file.
-- If you would like to see a picture of the model, print(model)
require 'torch'   -- torch
require 'image'   -- for image transforms
require 'nn'      -- provides all sorts of trainable modules/layers


noutputs = opt.noutput			-- # output nodes
nnodes = opt.nnodes
stdDev = opt.stdDev					-- std Dev. only used for cos when initializing weights/bias
nfeatures = opt.nfeatures		-- # of features that each data point has

-- Linear models with tanh activation
if opt.model == 'linear' then  
  model = nn.Sequential()
  local first = nn.Concat(1)
  for i=1,nnodes do
    local temp = nn.Sequential()
    local module = nn.Linear(nfeatures, 1)
    local weight = torch.Tensor(1, nfeatures)
    for j=1,nfeatures do
      weight[1][j] = torch.rand(1)*2-1 --j?
    end 
    module.weight = weight
    module.bias = torch.Tensor(1):fill(1)
    temp:add(module) 
    temp:add(nn.Tanh())
    first:add(temp)
  end 
  model:add(first)
  local weight = torch.Tensor(1, nnodes)
  for i=1,nnodes do
    weight[1][i] = torch.rand(1)*2-1
  end 
  model:add(nn.Linear(nnodes,noutputs))
  
-- Cosine models with cosine activation
elseif opt.model == 'cos' then
  require 'Cos'
  model = nn.Sequential()
  local first = nn.Concat(1)
  for i=1,nnodes do
    local temp = nn.Sequential()
    local module = nn.Linear(nfeatures, 1)
    local weight = torch.Tensor(1, nfeatures)
    for j=1,nfeatures do
      weight[1][j] = torch.normal(0,stdDev) --j?
    end 
    module.weight = weight
    module.bias = torch.Tensor(1):fill(2*math.pi*torch.normal(0,stdDev))
    temp:add(module) 
    temp:add(nn.Cos())
    first:add(temp)
  end 
  model:add(first)
  model:add(nn.Linear(nnodes, noutputs))

elseif opt.model == 'model' then

  -- Simple 2-layer neural network, with tanh hidden units
  model = nn.Sequential()
  model:add(nn.Reshape(nfeatures))
  model:add(nn.Linear(nfeatures,nnodes))
  model:add(nn.Tanh())
  model:add(nn.Linear(nnodes,noutputs))

elseif opt.model == 'convnet' then

   if opt.type == 'cuda' then
      -- a typical modern convolution network (conv+relu+pool)
      model = nn.Sequential()

      -- stage 1 : filter bank -> squashing -> L2 pooling -> normalization
      model:add(nn.SpatialConvolutionMM(nfeats, nstates[1], filtsize, filtsize))
      model:add(nn.ReLU())
      model:add(nn.SpatialMaxPooling(poolsize,poolsize,poolsize,poolsize))

      -- stage 2 : filter bank -> squashing -> L2 pooling -> normalization
      model:add(nn.SpatialConvolutionMM(nstates[1], nstates[2], filtsize, filtsize))
      model:add(nn.ReLU())
      model:add(nn.SpatialMaxPooling(poolsize,poolsize,poolsize,poolsize))

      -- stage 3 : standard 2-layer neural network
      model:add(nn.View(nstates[2]*filtsize*filtsize))
      model:add(nn.Dropout(0.5))
      model:add(nn.Linear(nstates[2]*filtsize*filtsize, nstates[3]))
      model:add(nn.ReLU())
      model:add(nn.Linear(nstates[3], noutputs))

   else
      -- a typical convolutional network, with locally-normalized hidden
      -- units, and L2-pooling

      -- Note: the architecture of this convnet is loosely based on Pierre Sermanet's
      -- work on this dataset (http://arxiv.org/abs/1204.3968). In particular
      -- the use of LP-pooling (with P=2) has a very positive impact on
      -- generalization. Normalization is not done exactly as proposed in
      -- the paper, and low-level (first layer) features are not fed to
      -- the classifier.

      model = nn.Sequential()

      -- stage 1 : filter bank -> squashing -> L2 pooling -> normalization
      model:add(nn.SpatialConvolutionMM(nfeats, nstates[1], filtsize, filtsize))
      model:add(nn.Tanh())
      model:add(nn.SpatialLPPooling(nstates[1],2,poolsize,poolsize,poolsize,poolsize))
      model:add(nn.SpatialSubtractiveNormalization(nstates[1], normkernel))

      -- stage 2 : filter bank -> squashing -> L2 pooling -> normalization
      model:add(nn.SpatialConvolutionMM(nstates[1], nstates[2], filtsize, filtsize))
      model:add(nn.Tanh())
      model:add(nn.SpatialLPPooling(nstates[2],2,poolsize,poolsize,poolsize,poolsize))
      model:add(nn.SpatialSubtractiveNormalization(nstates[2], normkernel))

      -- stage 3 : standard 2-layer neural network
      model:add(nn.Reshape(nstates[2]*filtsize*filtsize))
      model:add(nn.Linear(nstates[2]*filtsize*filtsize, nstates[3]))
      model:add(nn.Tanh())
      model:add(nn.Linear(nstates[3], noutputs))
   end
else

   error('unknown -model')

end



----------------------------------------------------------------------
--print '==> here is the model:'
--print(model)
----------------------------------------------------------------------
-- Visualization is quite easy, using itorch.image().
--[[
if opt.visualize then
   if opt.model == 'convnet' then
      if itorch then
	 print '==> visualizing ConvNet filters'
	 print('Layer 1 filters:')
	 itorch.image(model:get(1).weight)
	 print('Layer 2 filters:')
	 itorch.image(model:get(5).weight)
      else
	 print '==> To visualize filters, start the script in itorch notebook'
      end
   end
end
]]--
