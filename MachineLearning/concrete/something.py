import numpy as np
import sys
import matplotlib.pyplot as plt
from objectreader import Reader

def LinearReg(Xtrain, Ttrain, Xtest, Lambda):
  N = np.size(Xtrain, axis=0)
  N2 = np.size(Xtest, axis=0)
  M = np.size(Xtrain, axis=1)

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
  
  testPhi = Xtest
  tileMT2 = np.tile(Phimean, (N2,1))
  testPhi = np.subtract(testPhi, tileMT2)
  testPhi = np.divide(testPhi, Phistd)
  testPhi = np.c_[np.ones(N2),testPhi]

  # calculate ts
  T = np.dot(testPhi,weights)

  return T

def Kernel(Xtrain, Ttrain, Xtest, Lambda):
  N = np.size(Xtrain, axis=0)
  N2 = np.size(Xtest, axis=0)
  M = np.size(Xtrain, axis=1)

  # is used to generate the covariance matrix
  cov = np.dot(.5, np.eye(M, M))

  # build NxN K matrix
  K = np.ones((N,N))
  for i in range(N):
    for j in range (N):
      temp = Xtrain[i,:] - Xtrain[j,:]
      temp.shape = (1,M)
      tempT = np.transpose(temp)
      temp2 = np.dot(temp, cov)
      temp = np.dot(temp2, tempT)
      temp = np.dot((-.5), temp)
      K[i,j] = np.exp(temp)

  # build training portion of equation 6.9
  I = np.eye(N,N)
  temp = np.dot(Lambda,I)
  add = np.add(K, temp)
  inv = np.linalg.inv(add)
  half = np.dot(inv, Ttrain)

  testResults = np.ones((1, N)) 
  # build prediction portion of equation 6.9, then make predictions
  for i in range(N2):
    for j in range(N):
      temp = Xtest[i,:] - Xtrain[j,:]
      temp.shape = (1,M)
      tempT = np.transpose(temp)
      temp2 = np.dot(temp, cov)
      temp = np.dot(temp2, tempT)
      temp = np.dot((-.5), temp)
      testResults[0,i] = np.exp(temp)

  testResults = np.dot(testResults, half)
  return testResults

def BestFit(read):
  # stores the best Lambda for the data, and the lowest error value
  lowestError = sys.float_info.max
  bestLambda=0
  # Respective errors for each lambda value
  lambdas=[]
  objVals=[]

  ### If you take the split out of the loop the graph flattens out ###

  for Lambda in np.linspace(0, 100,20):
    # 1. Get new lambda
    errors=[]
    # 2. Split data i different times, averaging error for all of those splits
    for i in range(200):
      Xtrain, Ttrain, Xtest, Ttest = read.split(.75)
      currentError = 0
      if function == "linear":
        Tpred = LinearReg(Xtrain, Ttrain, Xtest, Lambda)
      elif function == "kernel":
        Tpred = Kernel(Xtrain, Ttrain, Xtest, Lambda)
      errors.append((Ttest - Tpred)**2)

    # now have all the errors for all of the splits
    objVals.append(np.mean(errors))
    lambdaError = np.mean(errors)
    lambdas.append(Lambda)
    if lambdaError < lowestError:
      lowestError = lambdaError
      bestLambda=Lambda

  print bestLambda,lowestError
  plt.plot(lambdas,objVals)
  plt.show()


def supHelp():
  print ("\nArgument format: CSVfilename function")
  print ("For more information type: -h function")
  print ("Functions to call can include:")
  print ("\tlinear")
  print ("\tkernel")

def supHelpFunc(function):
  if function == "linear":
    print("This function...")
  elif function == "kernel":
    print("This function...")
  else:
    print("Invalid function name")

if len(sys.argv) == 1 or len(sys.argv) == 2:
  supHelp()
else:
  filename = sys.argv[1]
  if filename == "-h":
    if len(sys.argv) == 3:
      supHelpFunc(sys.argv[2])
    else:
      supHelp()
  else:
    function = sys.argv[2]
    read = Reader(filename)
    BestFit(read)
