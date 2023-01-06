import numpy as np
import sys
import matplotlib.pyplot as plt
from objectreader import Reader

filename = sys.argv[1]
read = Reader(filename)

"""
  Function that will compute prediction values for data.
  Parameters: X input variables with cooresponding T values
              X test variables
              Lambda value
  Returns: Array of predictions for each X test variable
"""
def LinearReg(Xtrain, Ttrain, Xtest, Lambda):
  """
  # determine mu for each of the M demensions
  muVector = np.mean(Xtrain, axis=0)
  # get covariance matrix from Xtrain
  cov = np.cov(Xtrain,rowvar=0)
  cov = np.linalg.inv(cov)
  # build Xtrain Phi
  Phi = np.ones((N,M))
  for row in range(N):
    temp = Xtrain[row,:] - muVector
    temp.shape = (1,M)
    temp = np.dot(np.dot(temp,cov), np.dot((Xtrain[row,:] - muVector),-.5))
    Phi[row,:] = np.exp(temp)
  """
  Phi = Xtrain
  # normalize features and add bias feature
  Phimean = np.mean(Phi, axis=0)
  Phistd  = np.std(Phi, axis=0)
  tileMT  = np.tile(Phimean, (N,1))
  Phi     = np.subtract(Phi, tileMT)
  Phi     = np.divide(Phi, Phistd)
  Phi     = np.c_[np.ones(N),Phi]

  # compute weights
  unit    = np.eye(M+1,M+1)
  temp    = np.dot(Lambda, unit)
  temp    = np.linalg.inv(np.add(temp, np.dot(Phi.T, Phi)))
  weights = np.dot(temp, np.dot(Phi.T, Ttrain))
  
  """
  # build testPhi
  testPhi = np.ones((N2, M))
  for row in range(N2):
    temp = Xtest[row,:] - muVector
    temp.shape = (1,M)
    temp = np.dot(np.dot(temp,cov), np.dot((Xtest[row,:] - muVector),-.5))
    testPhi[row,:] = np.exp(temp)
  """

  testPhi = Xtest
  tileMT2 = np.tile(Phimean, (N2,1))
  testPhi = np.subtract(testPhi, tileMT2)
  testPhi = np.divide(testPhi, Phistd)
  testPhi = np.c_[np.ones(N2),testPhi]

  # calculate ts
  T = np.dot(testPhi,weights)

  return T

"""
    Finds best lambda value for the testing data
    Checks upper-bound lambda (ensure that it does not overfit)
    Checks lower-bound lambda 
"""
#Do we want .75 to be an argument?
xs, ts, tx, tt = read.split(.75)

N = np.size(xs, axis=0)
N2 = np.size(tx, axis=0)
M = np.size(xs, axis=1)

bestFit = 0
Lambda = .00001
currentError = 0
lowestError = sys.float_info.max
bestLambda=0
lambdas=[]
objVals=[]

# find upper-bound lambda
for Lambda in np.linspace(0,50):
  errors=[]
  for i in range(5):
    currentError = 0
    tp = LinearReg(xs, ts, tx, Lambda)
    for row in range(N2):
      currentError = currentError + ((tt[row,:]-tp[row,:])**2)
    errors.append(currentError)
    #print errors
  lambdas.append(Lambda)
  objVals.append(np.mean(errors))
  if np.mean(errors) < lowestError:
    lowestError = np.mean(errors)
    bestLambda=Lambda

print bestLambda,lowestError
plt.plot(lambdas,objVals)
plt.show()
