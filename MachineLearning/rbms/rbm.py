import numpy as np
from random import random
from itertools import product
from sklearn.datasets import fetch_mldata

def sigmoid(z):
  '''
  Applies the logistic sigmoid. z can be an array
  '''
  return 1.0/(1.0+np.exp(-1*z))

class RBM:
  def __init__(self, numVisible, numHidden):
    self.nV=numVisible
    self.nH=numHidden
    self.W=np.zeros((numVisible,numHidden)) #indexed by visible,hidden
    self.B=np.zeros((numVisible,1))
    self.C=np.zeros((numHidden,1))
    self.V=np.zeros((numVisible,1))
    self.H=np.zeros((numHidden,1))

  def sampleVisible(self):
    '''
    Gibbs samples visible nodes from hidden nodes and parameters
    '''
    Vp=np.dot(self.W,self.H)+self.B
    Vp=sigmoid(Vp) #Vp is a column vector
    for i in range(self.nV):
      self.V[i,0]=1 if random()<Vp[i,0] else 0

  def sampleHidden(self):
    '''
    Gibbs samples hidden nodes from visible nodes and parameters
    '''
    Hp=np.dot(self.V.T,self.W)+self.C.T
    Hp=sigmoid(Hp) #Hp is a row vector
    for i in range(self.nH):
      self.H[i,0]=1 if random()<Hp[0,i] else 0

  def energy(self):
    '''
    Calculates the energy of the RBM
    '''
    return np.dot(np.dot(self.V.T,self.W),self.H)+np.dot(self.B.T,self.V)+np.dot(self.C.T,self.H)

class ParallelRBM:
  def __init__(self, numVisible, numHidden, Ts):
    self.nV=numVisible
    self.nH=numHidden
    self.Ts=Ts
    self.nTs=len(Ts)
    self.W=np.zeros((numVisible,numHidden)) #indexed by visible,hidden
    self.B=np.zeros((numVisible,1))
    self.C=np.zeros((numHidden,1))
    self.Vs=np.zeros((numVisible,self.nTs))
    self.Hs=np.zeros((numHidden,self.nTs))

  def gibbs(self):
    Hp=np.dot(self.W.T,self.Vs)+np.tile(self.C,(1,self.nTs)) #each column is a rbm
    Hp=sigmoid(Hp)
    for i,j in product(*[range(self.nH),range(self.nTs)]):
      self.Hs[i,j]=1 if random()<Hp[i,j] else 0

    Vp=np.dot(self.W,self.Hs)+np.tile(self.B,(1,self.nTs)) #each column is a rbm
    Vp=sigmoid(Vp)
    for i,j in product(*[range(self.nV),range(self.nTs)]):
      self.Vs[i,j]=1 if random()<Vp[i,j] else 0

  def energies(self):
    out=np.zeros((self.nTs,1))
    for i in range(self.nTs):
      out[i,0]=np.dot(np.dot(self.Vs.T[i,:],self.W),self.Hs[:,i])
    out=out+np.dot(self.Vs.T,self.B)+np.dot(self.Hs.T,self.C)
    return out

  def fitParallel(self, data, k):
    for row in range(np.shape(data)[0]): #for each row,
      self.Vs=np.tile(data[row,:],(self.nTs,1)).T #set each v to be the data point
      for i in range(k):
        self.gibbs()
      E=self.energies()
      for r in range(2,self.nTs,2):
        p=min(1,np.exp((1/self.Ts[r]-1/self.Ts[r-1])*(E[r]-E[r-1])))
        if random()<p:
          t=E[r]
          E[r]=E[r-1]
          E[r-1]=t
          t=self.Vs[:,r]
          self.Vs[:,r]=self.Vs[:,r-1]
          self.Vs[:,r-1]=t
          t=self.Hs[:,r]
          self.Hs[:,r]=self.Hs[:,r-1]
          self.Hs[:,r-1]=t
      for r in range(3,self.nTs,2):
        p=min(1,np.exp((1/self.Ts[r]-1/self.Ts[r-1])*(E[r]-E[r-1])))
        if random()<p:
          t=E[r]
          E[r]=E[r-1]
          E[r-1]=t
          t=self.Vs[:,r]
          self.Vs[:,r]=self.Vs[:,r-1]
          self.Vs[:,r-1]=t
          t=self.Hs[:,r]
          self.Hs[:,r]=self.Hs[:,r-1]
          self.Hs[:,r-1]=t


mnist = fetch_mldata('MNIST original')
data=mnist.data[np.where(mnist.target==0.)[0],:]
data=data[:5,:]
p=ParallelRBM(784,200,[1,2,3,4])
p.fitParallel(data,1)
