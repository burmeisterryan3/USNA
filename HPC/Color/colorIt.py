#MIDN Burmeister
#Professor Albing

import Image
import numpy as np
import scipy as sp
import sys
import matplotlib.pyplot as plt
import csv
import Image

#Open necessary files
dataFile=open(sys.argv[1])
reader=csv.reader(dataFile,delimiter=',',skipinitialspace=True)

#Initialize necessary data
nrows=ncols=maxVal=0

#Read in data file and store in array
#Need to keep track of number of rows and columns for later use
data=[]
for row in reader:
  row=map(int,row)
  ncols=len(row)
  data.append(row)
  for column in range (ncols):
    if row[column] > maxVal:
      maxVal=row[column]
  nrows=nrows+1
data=np.array(data)

imgplot=plt.imshow(data,cmap=plt.cm.spectral,interpolation='gaussian',vmin=-3,vmax=3)
#plt.colorbar()
plt.show()
