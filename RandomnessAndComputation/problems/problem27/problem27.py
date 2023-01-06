import sys

m = int(sys.argv[1])
a = int(sys.argv[2])
X = [int(sys.argv[3])]

for i in range(499):
  X.append((a*X[i])%m)
print X
