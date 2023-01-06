#MIDN Burmeister
#Professor Albing

import Image
import numpy as np
import sys
import matplotlib.pyplot as plt
import csv
import Image
from matplotlib import colors

#Open necessary files
dataFile=open(sys.argv[1])
reader=csv.reader(dataFile,delimiter=',',skipinitialspace=True)

fname=sys.argv[1][:-4]+".png"

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

img=plt.imshow(data, cmap='YlOrRd', interpolation='none', norm=norm)
plt.colorbar(img)
plt.savefig(fname)
