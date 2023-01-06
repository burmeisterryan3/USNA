from itertools import islice

def Lehmer(seed, a, m=6):
  array = []
  prev = seed
  for i in range(40):
    nextVal = (a*prev)%m
    array.append(nextVal)
    prev = nextVal
  print array

def Mixed(seed, a, c, m=6):
  array = []
  prev = seed
  for i in range(1,65537):
    nextVal = (a*prev + c)%m
    array.append(nextVal)
    prev = nextVal
  print len(set(array))

def check(a):
  for i in range(1,65537):
    if a**i % 65537 == 1:
      print i
      break
