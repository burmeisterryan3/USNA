-- This file tests data on the previously trained model
-- Can output the confusion matrix to stdout or save it to a file. Can also save the 
-- trained model s.t it can be loaded and used at a later time.
-- If this file is ran standalone, command line argument must be passed in.
-- Otherwise a table opt must specifcy the options for this file.
require 'torch'   -- torch
require 'xlua'    -- xlua provides useful tools, like progress bars
require 'optim'   -- an optimization package, for online and batch methods

----------------------------------------------------------------------

-- test function
function test()
   
  -- averaged param use?
   if average then
      cachedparams = parameters:clone()
      parameters:copy(average)
   end

   -- set model to evaluate mode (for modules that differ in training and testing, like Dropout)
   model:evaluate()

   -- test over test data
   for t = 1,testData:size() do

      -- get new sample
      local input = testData.data[t]
      if opt.type == 'double' then input = input:double()
      elseif opt.type == 'cuda' then input = input:cuda() end
      local target = testData.labels[t]

      -- test sample
      local pred = model:forward(input)
      confusion:add(pred, target)
   end

   -- update log/plot
   confusion:updateValids()
   acc = confusion.totalValid * 100

   -- averaged param use?
   if average then
      -- restore parameters
      parameters:copy(cachedparams)
   end
   
   -- next iteration:
   confusion:zero()
  
  return string.format("%.4f",acc)
end
