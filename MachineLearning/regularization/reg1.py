import numpy as np
import sys
import matplotlib.pyplot as plt

lamda = float(sys.argv[1])
N = 60
M = 65
M1 = 7
N2 = 50


# generate x's, mu, and t's
xs = np.random.rand(N,1)
mu = np.linspace(0,1,M)
ts = np.sin(2*np.pi*xs) + np.random.normal(scale=.15, size=(N,1))
var = .5

"""
# build train Phi
Phi = np.ones((N,M))
for row in range(N):
  Phi[row,:] = xs[row,:]
  for column in range(M):
    temp = (Phi[row,column] - mu[:,column])**2
    temp = temp/(2*(var**2))
    Phi[row,column] = np.exp(-temp)
"""

Phi = np.ones((N,M))
for row in range(N):
  Phi[row,:] = xs[row,:]
  Phi[row,:] = np.subtract(Phi[row,:],mu)
Phi = np.square(Phi)
Phi = np.divide(Phi,(2*(var**2)))
Phi = np.exp(-Phi)

# get means and std for each column of Phi
# regularize features
# don't have to tile std, but have to tile means
means = np.mean(Phi, axis=0)
std = np.std(Phi, axis=0)
tiledmeans = np.tile(means, (N,1))
Phi = np.subtract(Phi,tiledmeans)
Phi = np.divide(Phi,std)

# build weights
unit = np.eye(M,M)
temp = np.dot(lamda,unit)
temp = np.linalg.inv(np.add(temp, np.dot(Phi.T, Phi)))
w = np.dot(temp, np.dot(Phi.T, ts))

# add bias feature to the Training Phi
Phi = np.c_[np.ones(N),Phi]


# build test Phi from N2 input values
# regularize features from training means and std
txs = np.random.rand(N2,1)
tPhi = np.ones((N2,M))
for row in range(N2):
  tPhi[row,:] = txs[row,:]
  tPhi[row,:] = np.subtract(tPhi[row,:],mu)
tPhi = np.square(tPhi)
tPhi = np.divide(tPhi,(2*(var**2)))
tPhi = np.exp(-tPhi)

tiledmeans2 = np.tile(means, (N2,1))
tPhi = np.subtract(tPhi, tiledmeans2)
tPhi = np.divide(tPhi,std)

# calculate target values for test Phi
tt = np.dot(tPhi,w)

# add bias feature to the testing Phi
tPhi = np.c_[np.ones(N2),tPhi]

# plot the training x's and t's as circles
# plot the testing x's and calculated t's as x's
plt.plot(xs,ts,'o', txs, tt,'x')
plt.show()




















