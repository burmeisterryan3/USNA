a = [0, 0, 6, 4, 2]
x = [4, 5, 1, 3]

for i in range(4, 25):
  val = a[1]*x[i-1]+a[2]*x[i-2]+a[3]*x[i-3]+a[4]*x[i-4]
  x.append(val%7)

print(x)
