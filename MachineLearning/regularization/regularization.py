import numpy as np
import sys
import math
import matplotlib.pyplot as plt

#

lamda = float(sys.argv[1])
N = 20
M1 = 20


xtrain = np.random.rand(N,1)
xtrain.shape = (N,1)
xtrain = np.transpose(xtrain)

evenSpaces = np.linspace(0,1,20)
evenSpaces.shape = (20,1)
evenSpaces = np.transpose(evenSpaces)

locations = np.ones((N,1))
locations.shape = (N,1)
locations = np.transpose(locations)

phi = np.ones((N,1))
phi.shape = (N,1)
phi = np.transpose(locations)

var = 0.1**2

# get locations for 3.4
for index in range(20):
   temp = -1*(math.sqrt(evenSpaces[:,index]+(2*var)))
   locations[:,index] = temp + xtrain[:,index]

# fill up Phi
Phi = np.dot(locations.T, xtrain)

## build 3.28 ##
ttrain = np.sin(2*np.pi*xtrain.T) + np.random.normal(scale=.15, size=(N,1))
ttrain.shape = (N,1)
ttrain = np.transpose(ttrain)

I = np.eye((M1))
I = np.dot(I, lamda)
temp = np.dot(Phi.T, Phi)
temp = temp + I
temp = np.linalg.inv(temp)
temp2 = np.dot(Phi.T, ttrain.T)
w = np.dot(temp, temp2)
print w


# It took a little bit to get all of the matrixs to allign. I am getting 20 different values for the weights,
# but they are all negative, which should not be the case. I think the problem possible lies within how I
# calculate Phi on line 37, or with the linear algebra that I do from lines 46-50.


