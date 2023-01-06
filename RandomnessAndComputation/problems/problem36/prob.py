import random
import numpy as np
import sys

def findBlueHen():
  if int(random.uniform(0,540)) <= 2:
    return 1
  else:
    return  1 + findBlueHen()

temp = []
for i in range(50):
  temp.append(findBlueHen())
print np.mean(temp)
