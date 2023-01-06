#Code for the csvReader

import sys
import csv
import numpy as np
import random

csvfile = open(sys.argv[1])

reader=csv.reader(csvfile,delimiter=',',skipinitialspace=True)

#Will skip header... should check for header in future
next(reader)

data=[]
for row in reader:
  row = map(float,row)
  data.append(row)
data = np.array(data)

numData = len(data)
numFeatures = len(data[0])

trainPer = .75
numTrainData = int(numData * trainPer)
numTestData = numData - numTrainData
if (numTrainData + numTestData) < numData:
  numTestData = numTestData + 1

randomTrain = random.sample(range(0,1029), numTrainData)
randomTrain.sort()

training = np.ones((numTrainData, numFeatures))
testing = np.ones((numTestData, numFeatures))

trainRow = 0
testRow = 0
for row in range(numData):
  if trainRow < numTrainData and row == randomTrain[trainRow]:
    training[trainRow, :] = data[row, :]
    trainRow = trainRow + 1
  else:
    testing[testRow, :] = data[row, :]
    testRow = testRow + 1
     
trainingInput, trainingOutput = np.array_split(training, [numFeatures-1], axis=1)
testInput, testOutput = np.array_split(testing, [numFeatures-1], axis=1)

print np.shape(trainingInput)
print np.shape(trainingOutput)
print np.shape(testInput)
print np.shape(testOutput)
