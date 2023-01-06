import sys
import numpy as np
import matplotlib.pyplot as plt
from random import random
from sklearn.svm import SVC
from sklearn.cross_validation import train_test_split
from sklearn.cross_validation import cross_val_score
from sklearn.cross_validation import ShuffleSplit
from genData import genData

# Initialization of necessary variables
totMinError = sys.maxsize
c = np.linspace(.1, 10, 250)

# Generate and train on a data set
train,t=genData(int(sys.argv[1]),float(sys.argv[2]))
bestc = c[0]
nsamples = len(train)
# so cross_val_score uses the same random splits for each c[i]
cv = ShuffleSplit(nsamples, n_iter=5, test_size=.25, random_state=0)
for ci in c:
  svm = SVC(kernel='rbf', C=ci)
  svm.fit(train, t)
  scores = cross_val_score(svm, train, y=t, scoring='accuracy',cv=cv)
  error = np.mean(scores)
  if (error < totMinError):
    totMinError = error
    bestC = ci

# Generate test set and test on previously determined C
bestSvm = SVC(kernel='rbf', C=bestC)
bestSvm.fit(train, t)
N=(5*len(train))

# predict new data from prev. fit
data, t = genData(3000)
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

# Plot the results
plt.plot(xs1,ys1,'.',xs2,ys2,'.')
plt.show()
