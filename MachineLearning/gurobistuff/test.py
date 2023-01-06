import gurobipy as grb
import numpy as np

def modelFromArrays(H,f,A,b):
  n,k=np.shape(A)
  assert np.shape(H)==(k,k)
  assert (np.shape(f)==(1,k) or np.shape(f)==(k,1) or np.shape(f)==(2,))
  assert (np.shape(b)==(1,n) or np.shape(b)==(n,1) or np.shape(b)==(n,))

  gModel=grb.Model()

  for v in range(k):
    gModel.addVar(name='w'+str(v))
  gModel.update()

H=np.array([[1,2],[3,4]])
f=np.array([1,2])
A=np.array([[1,2],[3,4],[5,6]])
b=np.array([[1],[2],[3]])

modelFromArrays(H,f,A,b)
