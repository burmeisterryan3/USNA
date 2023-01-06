#!/usr/bin/python
import numpy as np
import sys
import matplotlib.pyplot as plt

n=int(sys.argv[1])
m=int(sys.argv[2])

xs=np.random.rand(n,1)
t=np.sin(2*np.pi*xs) + np.random.normal(scale=.15,size=(n,1))
print t


Phi=np.ones((n,m+1))
for column in range(1,m+1):
  Phi[:,column]=np.power(xs,column)[:,0]

w=np.linalg.pinv(Phi).dot(t)

<<<<<<< HEAD
testXs=np.linspace(0,1).T
#print testXs
=======
testXs=np.linspace(0,1,num=500).T
>>>>>>> 870685d3302d4b22499686fb25b81045cf774e7a
testPhi=np.ones((np.shape(testXs)[0],m+1))

for column in range(1,m+1):
  testPhi[:,column]=np.power(testXs,column)

#plots xs with t as a scatter plot,
#the true function in green
#the approximate function in red
<<<<<<< HEAD


xs = np.array([[.25],[.5],[.75]])
t = np.sin(2*np.pi*xs)


#plt.plot(xs,t,'o',testXs,np.sin(2*np.pi*testXs),testXs,testPhi.dot(w))

#plt.show()
=======
plt.plot(xs,t,'o',testXs,np.sin(2*np.pi*testXs),testXs,testPhi.dot(w))
plt.ylim([-1,1])
plt.show()
>>>>>>> 870685d3302d4b22499686fb25b81045cf774e7a
