import sys
import numpy as np
import matplotlib.pyplot as plt
from random import random
from sklearn.svm import LinearSVC
from sklearn.multiclass import OneVsOneClassifier

def genData(n, percentWrong=0):
  '''
  n is the number of data points to be generated.
  percentWrong is the portion that will be given the wrong label (0.0-1.0).
  '''
  data=np.zeros((n,2))
  t=np.zeros((n,))
  for i in range(n):
    data[i,0]=random()*10
    data[i,1]=random()*2-1
    x=data[i,0]
    y=data[i,1]
    t[i]=1 if y>np.sin(2*x/np.pi) else -1
    if random()<percentWrong:
      t[i]*=-1
  return data,t

if __name__=="__main__":
  '''
  usage as main method:
  python genData 1000 .1
  '''
  data,t=genData(int(sys.argv[1]),float(sys.argv[2]))
  x1=[]
  y1=[]
  x2=[]
  y2=[]
  for i in range(np.shape(t)[0]):
    if t[i]==1:
      x1.append(data[i,0])
      y1.append(data[i,1])
    else:
      x2.append(data[i,0])
      y2.append(data[i,1])


  xs, ts = np.array(data), np.array(t)

  svc = LinearSVC(C=1).fit(xs,ts)
  predictions = svc.predict(xs)


  # distance from the hyperplane
  distance = svc.decision_function(xs)
  distnace = np.ravel(distance)
  # constant in decision function
  print svc.intercept_
  # weights assigned to coefficients
  print svc.coef_





'''
  v = svc.fit_transform(xs,ts)
  plt.plot(v[:,0], v[:,1],'o')
  plt.show()
'''

#  plt.plot(x1,y1,'x',x2,y2,'o')
#  plt.show()
