'''SI 486H Spring 2016
   This is a skeleton for a skip list class that only supports
   insertion of height-1 nodes.
 
   Nodes are stored in "towers" which contain a list of forward-facing
   links ("flinks"), as well as a key and a value.
 
   To demo on a small random example, run 'python3 skiplist.py"
'''
 
import random, sys
 
class SkipList:
    class Tower:
        def __init__(self, h, key, value=None):
            self.key = key
            self.value = value
            self.flinks = [None] * (h+1)
            self.blinks = [None] * (h+1)
 
    def __init__(self):
        self.start = self.Tower(0, float('-inf'))
        self.end = self.Tower(0, float('+inf'))
        self.start.flinks[0] = self.end
        self.end.blinks[0] = self.start
        self.size = 0
 
    def height(self):
        return len(self.start.flinks) - 1
 
    def __len__(self):
        return self.size
 
    def insert(self, key, value=None):
        h, flip = 0,random.getrandbits(1)
        while flip == 0:
            h += 1
            flip = random.getrandbits(1)

        newTower = self.Tower(h, key, value)
 
        # Grow height of start/end as needed
        while h >= self.height():
            self.start.flinks.append(self.end)
            self.end.flinks.append(None)
            self.end.blinks.append(self.start)
            self.start.blinks.append(None)
 
        currentTower = self.start
        level = len(currentTower.flinks)-1
        while level >= 0:
            if key > currentTower.flinks[level].key:
                # Move right if we can on this level
                currentTower = currentTower.flinks[level]
            else:
                # Otherwise go down a level
                if level <= h:
                    # Insert the new tower on this level
                    newTower.flinks[level] = currentTower.flinks[level]
                    currentTower.flinks[level] = newTower
                    newTower.flinks[level].blinks[level] = newTower
                    newTower.blinks[level] = currentTower
                level -= 1
       
        self.size += 1

    def deleteMin(self):
        minTower = self.start.flinks
        priority = minTower[0].key
        value = minTower[0].value
        for i in range(len(minTower[0].flinks)):
            minTower[i].flinks[i].blinks[i] = self.start
            self.start.flinks[i] = minTower[i].flinks[i]
        self.size -= 1
        return priority, value
        

    def deleteMax(self):
        maxTower = self.end.blinks
        priority = maxTower[0].key
        value = maxTower[0].value
        for i in range(len(maxTower[0].blinks)):
            maxTower[i].blinks[i].flinks[i] = self.end
            self.end.blinks[i] = maxTower[i].blinks[i]
        self.size -= 1
        return priority, value

    def zombieCheck(self):
        currentTower = self.start.flinks
        prob = 1/float((currentTower[0].key + 1)**2)
        if random.random() < prob:
            name = currentTower[0].value
            for i in range(self.size):
                self.deleteMax()
            print('ZOMBIE ' + name)
 
    def __str__(self):
        '''Returns a nice pretty representation of the skip list'''
        toret = ""
        for level in reversed(range(self.height()+1)):
            currentTower = self.start
            while currentTower is not self.end:
                rep = '({})'.format(currentTower.key)
                if level >= len(currentTower.flinks):
                    rep = '-' * len(rep)
                toret = toret + rep + '--'
                currentTower = currentTower.flinks[0]
            toret = toret + '({})\n'.format(currentTower.key)
        return toret
 
if __name__ == '__main__':
    # Randomly insert 10 numbers and print the resulting skip list
    S = SkipList()
    maxNumPassengers = int(sys.argv[1])
    for line in sys.stdin:
        line = line.split()
        if line[0] == 'HUMAN':
            S.insert(float(line[1]), line[2])
            if S.size > maxNumPassengers:
                _, name = S.deleteMax()
                print('SICK ' + name)
        else:
            for i in range(int(line[1])):
                if S.size > 0:
                    _, name = S.deleteMin()
                    print('HEALED ' + name)
        S.zombieCheck()
