import sys
import numpy as np
import matplotlib.pyplot as plt
from sklearn.svm import SVC
from sklearn.cross_validation import train_test_split
from sklearn.cross_validation import cross_val_score
from sklearn.cross_validation import ShuffleSplit
from genData import genData
import collections

# Initialization of necessary variables
totMinError = sys.maxsize
c = np.linspace(.1, 10, 250)

# Generate and train on a data set
train,t=genData(int(sys.argv[1]),float(sys.argv[2]))
bestc = c[0]
# so cross_val_score uses the same random splits for each c[i]
cv = ShuffleSplit(len(train), n_iter=5, test_size=.25, random_state=0)
for ci in c:
  svm = SVC(kernel='rbf', C=ci)
  svm.fit(train, t)
  scores = cross_val_score(svm, train, y=t, scoring='f1',cv=cv)
  error = np.mean(scores)
  if (error < totMinError):
    totMinError = error
    bestC = ci

# Generate test set and test on previously determined C
bestSvm = SVC(kernel='rbf', C=bestC)
bestSvm.fit(train, t)
data, t = genData(10000)
predicted = bestSvm.predict(data)
xs1 = []
ys1 = []
xs2 = []
ys2 = []
for i in range(len(predicted)):
  if predicted[i] == 1:
    xs1.append(data[i,0])
    ys1.append(data[i,1])
  else:
    xs2.append(data[i,0])
    ys2.append(data[i,1])


sorty = []
sortx = []
graph = dict(zip(xs2, ys2))
for k in sorted(graph.keys()):
  sortx.append(k)
  sorty.append(graph[k])

boundx, boundy = [], []
tempx, tempy = [], []
mod = 1
for i in range(len(sortx)):
  tempx.append(sortx[i])
  tempy.append(sorty[i])
  if sortx[i] < 2:
    mod = i % 50
  if sortx[i] > 2 and sortx[i] < 4:
    mod = i % 100
  if sortx[i] > 4 and sortx[i] <= 6:
    mod = i % 30
  if sortx[i] > 8 and sortx[i] < 10:
    mod = i % 20
  if mod == 0:
    index = tempy.index(max(tempy))
    boundx.append(tempx[index])
    boundy.append(tempy[index])
    tempx = []
    tempy = []
 
# Plot the results
plt.plot(xs1,ys1,'.',xs2,ys2,'.', boundx, boundy, 'D')
plt.show()
