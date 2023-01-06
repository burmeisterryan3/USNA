import sys
import random

numStates = int(sys.argv[1])
numSamples = int(sys.argv[2])

nonZeroRewardStates = {}
for i in range(3,len(sys.argv),2):
  nonZeroRewardStates[int(sys.argv[i])] = float(sys.argv[i+1])

samples = []

for i in range(numSamples):
  state = random.randint(1, numStates)

  action = None
  nextState = None
  if random.random() > .9:
    action = 'left'
    nextState = (state - 1)% numStates
  else:
    action = 'right'
    nextState = (state + 1) % numStates

  reward = 0
  if state in nonZeroRewardStates:
    reward = nonZeroRewardStates[state]

  samples.append((state, action, reward, nextState))

print samples
