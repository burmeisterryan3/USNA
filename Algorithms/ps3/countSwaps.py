import math
import numpy as np
import sys

def frankenCount(n, count=0):
  m = math.ceil(float(n)/4)

  if n not in fc_table:
    if n <= 5:
      fc_table[n] = n-1
    else:
      count1 = count + 3*frankenCount(2*m, count)
      count2 = count + 2*frankenCount(n-2*m, count)
      count3 = count + m
      fc_table[n] = count1 + count2 + count3
  return fc_table[n]

fc_table = {}
n = long(sys.argv[1])
print frankenCount(n)
