local Cos, parent = torch.class('nn.Cos', 'nn.Module')

function Cos:updateOutput(input)
  self.output = input:cos()*math.sqrt(2)
  return self.output
end

function Cos:updateGradInput(input, gradOutput)
  -- Because there is a phase shift we need the input here
  local temp = input:clone()
  self.gradInput = torch.cmul(gradOutput * -1, temp:sin(temp))*math.sqrt(2)
  return self.gradInput
end
