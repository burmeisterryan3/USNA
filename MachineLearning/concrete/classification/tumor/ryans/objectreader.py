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

  def split(self):
    outs, ins = np.array_split(self.data, [1-self.numFeat], axis=1)
    return outs, ins
  
  def __init__(self,filename):
    self.numData = np.array([])
    self.numFeat = np.array([])
    self.data = self.readFile(filename)
