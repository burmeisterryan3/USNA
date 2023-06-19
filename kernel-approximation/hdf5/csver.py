import os
import sys

'''
USAGE: python csver.py ./path/to/output/directory
'''

filepath = sys.argv[1]

models = []
stdDev = []
numNodes = []
rankPerc = []
runTimes = []
numEpochs = 0
numIterations = []
weightDecay = []
learningRate = []

for filename in os.listdir(filepath):
  if filename.endswith('.o'):
    models.append(filename.split('_')[0])
    with open(filepath+filename, 'r') as openFile:
      numIterations.append(0)
      for line in openFile:
        rankPercT = []
        runTimesT = []
        numIterations[len(numIterations)-1] += 1
        for counter, num in enumerate(line.split()):
          if models[len(models)-1] == 'cos':
            if counter == 0:
              stdDev.append(num)
            elif counter == 1:
              numNodes.append(num) 
            elif counter == 2:
              learningRate.append(num)
            elif counter == 3:
              weightDecay.append(num)
            elif counter%2 == 0:
              rankPercT.append(num)
            else:
              runTimesT.append(num)
          else: # model = linear
            if counter == 0:
              numNodes.append(num)
              stdDev.append(-1)
            elif counter == 1:
              learningRate.append(num)
            elif counter == 2:
              weightDecay.append(num)
            elif counter%2 == 1:
              rankPercT.append(num)
            else:
              runTimesT.append(num)
           
        numEpochs = len(rankPercT)
        rankPerc.append(rankPercT)
        runTimes.append(runTimesT)

header = 'model, stdDev, numNodes, learningRate, weightDecay'
for i in range(numEpochs):
  header += ', rankPerc, runTime'

outfile = filepath + './results.csv'
with open(outfile, 'w') as out:
  out.write(header+'\n')
  totalIteration = 0
  for count, model in enumerate(models):
    for i in range(numIterations[count]):
      writeLine = model + ', ' + str(stdDev[totalIteration]) + ', ' + \
        str(numNodes[totalIteration]) + ', ' + \
        str(learningRate[totalIteration]) + ', ' + \
        str(weightDecay[totalIteration])
      for j in range(numEpochs):
        writeLine += ', ' + str(rankPerc[totalIteration][j]) + ', ' + \
          str(runTimes[totalIteration][j])
      out.write(writeLine + '\n')
      totalIteration += 1
