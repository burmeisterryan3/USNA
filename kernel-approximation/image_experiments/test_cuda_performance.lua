#!/usr/bin/env torch
require 'nn'
require 'image'
require 'xlua'
require 'pl'
 
 
opt = lapp[[
   -t,--threads            (default 1)           number of threads
   -p,--type               (default float)       float or cuda
   -i,--devid              (default 1)           device ID (if using CUDA)
   -n,--npix               (default 512)         the image size
   -r,--niter              (default 10)          the number of conv operations for profiling
   -m,--nimage             (default 1)           number of images
]]
 
p = xlua.Profiler()
torch.setnumthreads(opt.threads)
torch.manualSeed(1)
torch.setdefaulttensortype('torch.FloatTensor')
 
 
if opt.type == 'cuda' then
   print('==> switching to CUDA')
   require 'cunn'
   cutorch.setDevice(opt.devid)
   print('==> using GPU #' .. cutorch.getDevice())
 
   nn.SpatialConvolutionMM = nn.SpatialConvolution
end
 
-- input:
lena1 = torch.Tensor(opt.nimage,1,opt.npix,opt.npix)
-- lena1 = image.lena()[{1}]:reshape(1,512,512)
 
-- model to test:
--model = nn.SpatialConvolution(1, 8, 9, 9)
model = nn.SpatialConvolution(1, 16, 10, 10) -- 16 filters of 10x10 on a 512x512 image
 
-- copy to GPU if desired:
if opt.type == 'cuda' then
   model:cuda()
   lena1 = torch.CudaTensor(opt.nimage,opt.npix,opt.npix)
end
 
-- test speed:
p:start('spatialconv')
for iter=1,opt.niter do
   lena2 = model:forward(lena1)
   if opt.type == 'cuda' then cutorch.synchronize() end
end
p:lap('spatialconv')
 
p:printAll{}
 
print('cpu time = ',p:cpu('spatialconv'))
print('Gops/s:', opt.niter*( 16*10*10*((opt.npix-9)+1)*((opt.npix-9)+1)*2 ) / p:cpu('spatialconv') / 1e9 ) -- 2 operations MUL, ACC

