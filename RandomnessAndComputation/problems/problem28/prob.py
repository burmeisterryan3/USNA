def LCG(seed1, seed2):
    next1 = (5 * seed1 + 3) % 16
    next2 = (5 * seed2 + 9) % 16
    return next1, next2

num1, num2 = 2, 2
for i in range(16):
    num1, num2 = LCG(num1, num2)
    print(num1+num2)
    print(abs(num1-num2))
   #num1 = '{0:04b}'.format(seed1)
   #num2 = '{0:04b}'.format(seed2)
   #print(int(num1[1:3]+num2[1:3],2))

