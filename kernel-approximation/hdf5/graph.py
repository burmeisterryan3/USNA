import sys
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.cm as cm

'''
USAGE: python graph.py ./path/to/results.csv nameOfSavedGraph.png time
time is either 'epochs' or 'seconds', will determine x axis
'''

filepath = sys.argv[1]
graphpath = "/".join(filepath.split('/')[0:-1])+"/"+sys.argv[2]
xaxis = sys.argv[3]

filedata = open(filepath, 'r')
header = filedata.readline()

# subtract 2 for stdDev and numNodes, divide by two for perc and time each epoch
numEpochs = (len(header.split(","))-5)/2

perc, times, legend = [], [], []
for line in filedata:
  split = line.split(',')
  legendStr = ''
  if split[0] == 'cos':
    legendStr += split[0] + ' ' + split[1]
  else:
    legendStr += 'tanh'
  legendStr += ' ' + split[2] + ' ' + split[3] + ' ' + split[4]
  percT, timesT = [], []
  for i in range(2, numEpochs+2):
    percT.append(float(split[2*(i+1)-1]))
    timesT.append(int(split[2*(i+1)]))
  perc.append(percT)
  times.append(timesT)
  legend.append(legendStr) 

fig, ax = plt.subplots()
ax.set_title('Percentage correct vs time')
ax.set_ylabel('Accuracy (%)', fontsize=14)

totalTime = 0
mintime,maxtime = sys.maxint, -1*sys.maxint
minPerc,maxPerc = sys.maxint, -1*sys.maxint
for time, percs in zip(times, perc):
  if xaxis == 'seconds':
    ax.set_xlabel('Time (s)', fontsize=14)
    for i in range(len(time)):
      if i != 0:
        time[i] = time[i] + time[i-1] 
  else:
    ax.set_xlabel('Time (epochs)', fontsize=14)
    time = np.linspace(1,numEpochs,numEpochs)

  for percent in percs:
    if percent > maxPerc:
      maxPerc = percent
    if percent < minPerc:
      minPerc = percent
  if xaxis == 'seconds':
    if time[len(time)-1] > maxtime:
      maxtime = time[len(time)-1]
    if time[0] < mintime:
      mintime = time[0]
    ax.plot(time, percs, marker='o', linestyle='-')
  else:
    maxtime = time[len(time)-1]
    mintime = time[0]
    ax.plot(time, percs, linestyle='-')

if xaxis == 'seconds':
  ax.axis([mintime-50, maxtime+50, minPerc-3, maxPerc+3])
else:
  ax.axis([0, numEpochs+1, minPerc-3, maxPerc+3])
ax.legend(legend, loc='best', fancybox=True, shadow=True)
plt.savefig(graphpath)
plt.show()
