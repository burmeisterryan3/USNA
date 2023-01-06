import sys

flips = []
heights = {}

height = 0
for line in sys.stdin:
    flips.append(int(line))
    if flips[len(flips)-1] == 0:
        height = height + 1
    else:
        for h in range(height+1):
            heights[h] = heights[h] + 1 if h in heights.keys() else 1
        height = 0

for height in reversed(list(heights.keys())):
    print('Level %d: %d node%s' %(height, heights[height], '' if heights[height] == 1 else 's'))
