import numpy as np
import sys
import matplotlib.pyplot as plt

# Question: is this whole thing called: kernel functions with gaussian bias function?

lamda = float(sys.argv[1])
N = 100
N2 = 50
var = .4
sin = np.linspace(0,1,100)


### TRAINING ###
# get training xs and ts
xs = np.random.rand(N,1)
ts = np.sin(2*np.pi*xs) + np.random.normal(scale=.15, size=(N,1))
xsT = np.transpose(xs)

# tile xs to calculate training K
xcol = np.tile(xs, N)
xrow = np.tile(xsT, (1,N,1))

# build NxN K matrix
K = np.ones((N,N))
for i in range(N):
  temp = (xcol[i,:] - xrow[:,i])**2
  temp = np.divide(temp, (2*(var**2)))
  K[i,:] = np.exp(-temp)

# build training portion of equation 6.9
I = np.eye(N,N)
temp = np.dot(lamda,I)
train = np.add(K, temp)
train = np.linalg.inv(train)
train = np.dot(train, ts)


### PREDICTIONS ###
# get test xs 
xt = np.random.rand(N2,1)
t = np.ones((1,N2))

# tile test xs to calculate k(x).T
xtCol = np.tile(xt, N)

# build prediction portion of equation 6.9, then make predictions
#tK = np.ones((N2,N))
for i in range(N2):
  temp = (xrow[:,i] - xtCol[i,:])**2
  temp = np.divide(temp, (2*(var**2)))
  temp = np.exp(-temp)
  t[:,i] = np.dot(temp, train)

# plot data
fig, ax = plt.subplots()
ax.plot(xs, ts, 'ro', label='Training')
ax.plot(xt,t.T, 'D', label='Testing')
ax.plot(sin,np.sin(2*np.pi*sin), 'y', label='Sin(x)')

legend = ax.legend(loc='upper right', shadow=True)

plt.show()
