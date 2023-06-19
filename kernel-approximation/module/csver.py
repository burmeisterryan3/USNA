# converts the .o and .e messages from tests to csv files
import numpy as np
import sys
import os

'''
USAGE:  python csver.py ./path/to/output/
'''

# where the datafiles are located
filepath = sys.argv[1]

# extract data from files and build strings for csvwriter
epochs = ''
dataFiles, paraLines, accuracyLines = [], [], []
for filename in os.listdir(filepath):
  if filename.endswith('.o'):
    writeLine = ""
    parameters = filename.split('.')
    if parameters[1] == 'cos':
      writeLine+="Cosine, " + str(parameters[2])
    else: # Model is linear, no standard deviation
      writeLine+="Linear, "+str(parameters[2])
    paraLines.append(writeLine)
    openFile = open(filepath+filename, 'r')
    acc = openFile.read()
    epochs = int(acc.split()[acc.split().index('size')+1][0:-1])
    accavg = []
    for index in range(epochs):
       accavg.append(acc.split()[index])  
    accuracyLines.append(np.array(accavg))

# write all info to file
header='Model, stdDev(cosine), #Features, '
for i in range(epochs):
  header+=((str(i+1)+', ') if i != epochs-1 else str(i+1))
outfile = filepath+"./resultsTable.csv"
with open(outfile,'w') as out:
  out.write(header+'\n')
  for i,accuracy in enumerate(accuracyLines):
    writeLine = paraLines[i]
    writeLine += ", ".join(accuracy)+'\n'
    out.write(writeLine)    
