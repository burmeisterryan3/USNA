import numpy as np
import sys
import matplotlib.pyplot as plt
from objectreader import Reader

lamda = float(sys.argv[1])
filename = sys.argv[2]


read = Reader(filename)

xs = read.trainingInput
ts = read.trainingOutput
tx = read.testInput
tt = read.testOutput

N = np.size(xs, axis=0)
N2 = np.size(tx, axis=0)
M = np.size(xs, axis=1)
var = .5


"""
  Function that will compute prediction values for data.
  Parameters: X input variables with cooresponding T values
              X test variables
              Lamda value
  Returns: Array of predictions for each X test variable
"""
def LinearReg(Xtrain, Ttrain, Xtest, Ttest, Lamda):
  # determine mu for each of the M demensions
  muVector = np.mean(Xtrain, axis=0)

###
### USE THE NEW EQUATION IN THE BOOK TO CALC THIS
### THE ONE THAT HE TOLD USE TO USE IN EMAIL
###

  
  # build Xtrain Phi
  Phi = np.ones((N,M))
  for row in range(N):
    Phi[row,:] = Xtrain[row,:]
    Phi[row,:] = np.subtract(Phi[row,:],muVector)
  Phi = np.square(Phi)
  Phi = np.divide(Phi,(2*(var**2)))
  Phi = np.exp(-Phi)

  # normalize features and add bias feature
  Phimean = np.mean(Phi, axis=0)
  Phistd  = np.std(Phi, axis=0)
  tileMT  = np.tile(Phimean, (N,1))
  Phi     = np.subtract(Phi, tileMT)
  Phi     = np.divide(Phi, Phistd)
  Phi     = np.c_[np.ones(N),Phi]

  # compute weights
  unit    = np.eye(M+1,M+1)
  temp    = np.dot(Lamda, unit)
  temp    = np.linalg.inv(np.add(temp, np.dot(Phi.T, Phi)))
  weights = np.dot(temp, np.dot(Phi.T, Ttrain))

  # build testPhi
  testPhi = np.ones((N2, M))
  for row in range(N2):
    testPhi[row,:] = Xtest[row,:]
    testPhi[row,:] = np.subtract(testPhi[row,:], muVector)

  testPhi = np.square(testPhi)
  testPhi = np.divide(testPhi,(2*(var**2)))
  testPhi = np.exp(-testPhi)
  tileMT2 = np.tile(Phimean, (N2,1))
  testPhi = np.subtract(testPhi, tileMT2)
  testPhi = np.divide(testPhi, Phistd)
  testPhi = np.c_[np.ones(N2),testPhi]

  # calculate ts
  T = np.dot(testPhi,weights)

  return T


tp = LinearReg(xs, ts, tx, tt, lamda)
for row in range(N2):
  print tt[row,:],' ',tp[row,:]



# plot the training x's and t's as circles
# plot the testing x's and calculated t's as x's
plt.plot(tx,tp,'o', tx, tt,'x')# np.linspace(0,1,100), np.sin(2*np.pi*np.linspace(0,1,100)))
plt.show()




















