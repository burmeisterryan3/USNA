import numpy as np

n = [10, 100, 1000, 10000, 100000, 1000000]
total = []

for i in range(len(n)):
  c = 0
  for j in range(n[i]):
    xy=np.random.uniform(-1.0, 1.0, 2)
    d=np.sqrt(xy[0]**2+xy[1]**2)
    if d <= 1:
      c = c+1
  total.append((n[i], float(4*c)/n[i]))

print total
