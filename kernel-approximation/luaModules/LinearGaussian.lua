require 'nn'
local LinearGaussian, parent = torch.class('nn.LinearGaussian', 'nn.Linear')


function LinearGaussian:__init(inputSize, outputSize, stdv)
   if stdv then
      self.__stdv=stdv
   end
   parent.__init(self, inputSize, outputSize)
end   

function LinearGaussian:reset(stdv)
   if not stdv and not self.__stdv then
      stdv = 1./math.sqrt(self.weight:size(2))
   elseif not stdv then
      stdv = self.__stdv
   end
   self.weight:normal(0, stdv)
   self.bias:zero()
   return self
end


