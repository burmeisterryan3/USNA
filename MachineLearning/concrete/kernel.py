import numpy as np
import sys
import matplotlib.pyplot as plt
from objectreader import Reader

filename = sys.argv[1]
read = Reader(filename)

def Kernel(Xtrain, Ttrain, Xtest, Lamda):
  N = np.size(Xtrain, axis=0)
  N2 = np.size(Ttrain, axis=0)
  M = np.size(Xtrain, axis=1)

  # is used to generate the covariance matrix
  cov = np.dot(.25, np.eye(N, N))

  # did not cover entire matrix
  # build NxN K matrix
  K = np.ones((N,N))
  for i in range(N):
    for j in range (N):
      temp = Xtrain[i,:] - Xtrain[j,:]
      tempT = np.transpose(temp)
      temp2 = np.dot(tempT, cov)
      temp = np.dot(temp2, temp)
      temp = np.dot((-.5), temp)
      K[i,j] = np.exp(temp)
 
  # build training portion of equation 6.9
  I = np.eye(N,N)
  temp = np.dot(Lamda,I)
  add = np.add(K, temp)
  inv = np.linalg.inv(add)
  half = np.dot(inv, Ttrain)

  testResults = np.ones((N2, 1)) 
  # build prediction portion of equation 6.9, then make predictions
  for i in range(N2):
    for j in range(N2):
      temp = Xtest[i,:] - Xtrain[j,:]
      tempT = np.transpose(temp)
      temp2 = np.dot(tempT, cov)
      temp = np.dot(temp2, temp)
      temp = np.dot((-.5), temp)
      testResults[i,0] = np.exp(temp)

  return testResults


bestFit = tt
Lamda = .01
currentError = 0
lowestError = sys.float_info.max
while Lamda < 1:
  xs, ts, tx, tt = read.split(.75)
  tp = Kernel(xs, ts, tx, Lamda)
  for row in range(N2):
    currentError = currentError + ((tt[row,:]-tp[row,:])**2)/N2
  if currentError < lowestError:
    LowestError = currentError
    bestFit = tp
  Lamda = Lamda + .1

plt.plot(tx, bestFit, 'o', tx, tt, 'x')
plt.show()

"""
  # plot data
  fig, ax = plt.subplots()
  ax.plot(xs, ts, 'ro', label='Training')
  ax.plot(xt,t.T, 'D', label='Testing')
  ax.plot(sin,np.sin(2*np.pi*sin), 'y', label='Sin(x)')

  legend = ax.legend(loc='upper right', shadow=True)

  plt.show()"""
