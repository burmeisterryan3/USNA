import numpy as np
import sys
import matplotlib.pyplot as plt

lamda = float(sys.argv[1])

Ntrain = 150
Ntest = 50
var = .5
std = var**2


xstrain = np.random.rand(Ntrain,1)
ts = np.sin(2*np.pi*xstrain) + np.random.normal(scale=.15, size=(Ntrain,1))
xstrainT = np.transpose(xstrain)

xscol = np.tile(xstrain, Ntrain)
xsrow = np.tile(xstrainT, (1,Ntrain,1))

K=np.ones((Ntrain, Ntrain))
for i in range(Ntrain):
  temp = (xscol[i,:] - xsrow[:,i])**2
  temp = np.divide(temp, 2*std)
  K[i,:] = np.exp(-temp)

ident = np.eye(Ntrain, Ntrain)
lamdaMat = np.dot(lamda, ident)
add = np.add(K, lamdaMat)
inv = np.linalg.inv(add)
a = np.dot(inv, ts)

xstest = np.random.rand(Ntest,1)
output = np.ones((1, Ntest))
xtestCol = np.tile(xstest, Ntrain)

for i in range(Ntest):
  temp = (xsrow[:,i] - xtestCol[i,:])**2
  temp = np.divide(temp, 2*std)
  temp = np.exp(-temp)
  output[:,i] = np.dot(temp, a)

sinxs = np.linspace(0,1,100)
sinys = np.sin(2*np.pi*sinxs)

#plt.plot(sinxs, sinys, xstrain, ts, 'rx', xstest, tstest, 'o')
plt.plot(sinxs, sinys, xstrain, ts, 'rx', xstest, output.T, 'o')
plt.show()
