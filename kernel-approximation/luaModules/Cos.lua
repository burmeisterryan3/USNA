local Cos, parent = torch.class('nn.Cos', 'nn.Module')

function Cos:updateOutput(input)
  return self.output:cos(input)
end

function Cos:updateGradInput(input, gradOutput)
  -- Because there is a phase shift we need the input here
  local temp = input:clone()
  return self.gradInput:cmul(gradOutput * -1, temp:sin(temp))
end
