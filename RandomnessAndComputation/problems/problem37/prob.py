delMid = 3/541.
notDelMid = 538/541.

a = []
#for i in range(1,100000):
for i in range(1,181):
  #a.append(i*delMid*notDelMid**(i-1))
  a.append(delMid*notDelMid**(i-1))

x=0
for i in range(len(a)):
  x = a[i] + x
  if x > .5:
    print i
    break

#print sum(a)
#print a[180]
#print a.index(max(a))
