# Turns csv files into graphs. Intended to be used after csver.py is used
import numpy as np
import matplotlib.pyplot as plt
import sys

'''
USAGE: python graph.py ./path/to/data.csv nameofsavedgraph.png
'''

# where the datatable is located
filepath = sys.argv[1]
graphpath = "/".join(filepath.split('/')[0:-1])+"/"

filedata = open(filepath, 'r')
header = filedata.readline()
epochs = int(header.split()[len(header.split())-1])

# build the legend titles and convert string percentages to floats
modelPerc, legendTitles = [], []
for model in filedata:
  legendstr = ""
  split = model.split(',')
  cosine=(False if split[0] == 'Linear' else True)
  legendstr+=split[0]
  legendstr+=((split[1]+" "+split[2]) if cosine else (split[1]))
  legendTitles.append(legendstr) 
  numbers = (([float(x) for x in split[3:len(split)]]) if cosine else \
            ([float(x) for x in split[2:len(split)]]))
  modelPerc.append(numbers)


# initialize the graph axes/title
fig, ax = plt.subplots()
ax.set_title('Kernelized vs. Linear')
ax.set_ylabel('Accuracy (%)', fontsize=14)
ax.set_xlabel('Epoch', fontsize=14)

# range for the x and y axis respectively 
ax.axis([0,9, 50,100])
ax.set_xticks(np.arange(0,10,1.0))
ax.set_yticks(np.arange(50,110,10))

# loop through the percentages for each model
for percentages in modelPerc:
  ax.plot(np.arange(0,epochs,1), percentages)

# location of the legend
ax.legend(legendTitles, loc=4)
plt.savefig(graphpath+sys.argv[2])
plt.show()
