----------------------------------------------------------------------
-- parse command-line options
--
dname,fname = sys.fpath()
cmd = torch.CmdLine()
cmd:text()
cmd:text('HouseNumber Training')
cmd:text()
cmd:text('Options:')
cmd:option('-save', fname:gsub('.lua',''), 'subdirectory to save/log experiments in')
cmd:option('-network', '', 'reload pretrained network')
cmd:option('-extra', false, 'use extra training samples dataset (~500,000 extra training samples)')
cmd:option('-visualize', false, 'visualize input data and weights during training')
cmd:option('-seed', 1, 'fixed input seed for repeatable experiments')
cmd:option('-plot', true, 'live plot')
cmd:option('-optimization', 'SGD', 'optimization method: SGD | ASGD | CG | LBFGS')
cmd:option('-learningRate', 1e-3, 'learning rate at t=0')
cmd:option('-batchSize', 1, 'mini-batch size (1 = pure stochastic)')
cmd:option('-weightDecay', 0, 'weight decay (SGD only)')
cmd:option('-momentum', 0, 'momentum (SGD only)')
cmd:option('-t0', 1, 'start averaging at t0 (ASGD only), in nb of epochs')
cmd:option('-maxIter', 2, 'maximum nb of iterations for CG and LBFGS')
cmd:option('-threads', 2, 'nb of threads to use')
cmd:option('-useKernel', false, 'use cos kernel')
cmd:option('-kernelStd', 0, 'kernel weight initialization stdDev')
cmd:text()
opt = cmd:parse(arg)

--Print some info
if opt.useKernel then
   print('USING KERNEL LAYER(S)')
end
if opt.maxIter then
   print('Max Iters = '..opt.maxIter )
end

dofile 'train_housenumbers_func.lua'
local accuracy=runIt(opt)
print('ALL DONE:',accuracy)
