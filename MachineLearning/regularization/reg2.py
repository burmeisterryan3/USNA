import  numpy as np
import matplotlib.pyplot as plt
import sys

lamda = float(sys.argv[1])

N=60
M=65
std=.25


#Develop Phi from gaussian basis function
xs = np.random.rand(N,1)
evenspaces = np.linspace(0,1,M)

diff = np.ones((N,M))

for row in range(N):
  diff[row, :] = xs[row,:]
  diff[row, :] = np.subtract(diff[row, :], evenspaces)

squared = np.square(diff)
divided = np.divide(squared, 2*std)
phi = np.exp(-divided)

#Regularization
colMean = np.mean(phi, axis=0)
colStd = np.std(phi, axis=0)

tiledMean = np.tile(colMean, (N,1)) #have to tile means
phi = np.subtract(phi, tiledMean)
phi = np.divide(phi, colStd) #do not have to tile std

#Include bias feature
phi = np.c_[np.ones(N),phi]

#Develop Weights
phiT = np.transpose(phi)
t = 2*np.ones((N,1)) + np.sin(2*np.pi*xs) + np.random.normal(scale=.15,size=(N,1))
second = np.dot(phiT, t)

ident = np.eye(M+1,M+1)
lamdaMatrix = np.dot(lamda, ident)
temp = np.dot(phiT, phi)
temp = np.add(lamdaMatrix, temp)
first = np.linalg.inv(temp)

weights = np.dot(first, second)


#Build test phi
Ntest = 50
testxs = np.random.rand(Ntest,1)

testDiff = np.ones((Ntest,M))

for row in range(Ntest):
  testDiff[row, :] = testxs[row,:]
  testDiff[row, :] = np.subtract(testDiff[row, :], evenspaces)

testSquared = np.square(testDiff)
testDivided = np.divide(testSquared, 2*std)
testPhi = np.exp(-testDivided)

#Test Regularization
tiledMeanTest = np.tile(colMean, (Ntest,1))

testPhi = np.subtract(testPhi, tiledMeanTest)
testPhi = np.divide(testPhi, colStd) #do not have to tile std

#Include bias feature
testPhi = np.c_[np.ones(Ntest),testPhi]

target = np.dot(testPhi, weights)

print weights


plt.plot(xs, t, 'rx', testxs, target, 'o')
plt.show()
