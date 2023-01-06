#Code for the csvReader

import sys
import csv
import numpy as np
import random

class Reader(object):
  def readFile(self, filename):
		csvfile = open(filename)
		reader=csv.reader(csvfile,delimiter=',',skipinitialspace=True)


		data = []
		# read in the file and change M and B to 0 and 1
		for row in reader:
			if row[1] == 'M':
				row[1] = 0
			else:
				row[1] = 1
			row = map(float, row)
			data.append(row)

		# remove the patient number
		rm = np.array(data)		
		rm = np.delete(rm,0,axis=1)

		self.numData = len(rm)
		self.numFeat = len(rm[0])

		return rm

  def split(self, trainPer):
    numTrainData = int(self.numData * trainPer)
    numTestData = self.numData - numTrainData
    
    # Ensure that all the data is accounted for
    if (numTrainData + numTestData) < self.numData:
      numTestData = numTestData + 1

    randomTrain = random.sample(range(0,self.numData), numTrainData)
    randomTrain.sort()

    training = np.ones((numTrainData, self.numFeat))
    testing = np.ones((numTestData, self.numFeat))

    trainRow = 0
    testRow = 0
    for row in range(self.numData):
      if trainRow < numTrainData and row == randomTrain[trainRow]:
        training[trainRow, :] = self.data[row, :]
        trainRow = trainRow + 1
      else:
        testing[testRow, :] = self.data[row, :]
        testRow = testRow + 1
         
    trainingOutput, trainingInput =\
       np.array_split(training, [1-self.numFeat], axis=1)
    testOutput, testInput =\
       np.array_split(testing, [1-self.numFeat], axis=1)
    return trainingInput, trainingOutput, testInput, testOutput

  def __init__(self,filename):
    self.numData = np.array([])
    self.numFeat = np.array([])
    self.data = self.readFile(filename)
