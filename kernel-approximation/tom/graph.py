#import matplotlib.pyplot as plt

f = open('output/example.txt', 'r')

percents = []
times = []
for i, line in enumerate(f):
  if i % 2 == 0:
    stdDev = float(line)
  else:
    numbers = [float(x) for x in line.split()]
    percents.append(numbers[0])
    times.append(numbers[1])

minTime = min(percents)
maxTime = max(times)

#plt.plot(times, percents, 'bx')
#plt.ylim(0, 100)
#plt.show()
