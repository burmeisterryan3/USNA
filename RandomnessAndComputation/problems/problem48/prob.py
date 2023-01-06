'''SI 486H Spring 2016
   This is a skeleton for a skip list class that only supports
   insertion of height-1 nodes.

   Nodes are stored in "towers" which contain a list of forward-facing
   links ("flinks"), as well as a key and a value.

   To demo on a small random example, run 'python3 skiplist.py"
'''

import random

class SkipList:
    class Tower:
        def __init__(self, h, key, value=None):
            self.key = key
            self.value = value
            self.flinks = [None] * (h+1)

    def __init__(self):
        self.start = self.Tower(0, float('-inf'))
        self.end = self.Tower(0, float('+inf'))
        self.start.flinks[0] = self.end
        self.size = 0

    def height(self):
        return len(self.start.flinks) - 1

    def __len__(self):
        return self.size

    def insert(self, key, value=None):
        h,flip = 0, random.getrandbits(1)
        while flip == 0:
            h += 1
            flip = random.getrandbits(1)
            
        newTower = self.Tower(h, key, value)

        # Grow height of start/end as needed
        while h >= self.height():
            self.start.flinks.append(self.end)
            self.end.flinks.append(None)

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
                level -= 1
        
        self.size += 1
    
    def search(self, key):
        currentTower = self.start
        level = self.height()
        while level >= 0:
            nextTowerKey = currentTower.flinks[level].key
            if key == nextTowerKey:
                return currentTower.flinks[level].value
            elif key > nextTowerKey:
                currentTower = currentTower.flinks[level]
            else: # key < nextTowerKey
                level -= 1
        return 'NOT FOUND'

    def delete(self, key):
        currentTower = self.start
        level = self.height()
        retVal = 'NOT FOUND'
        while level >= 0:
            nextTowerKey = currentTower.flinks[level].key
            if key == nextTowerKey:
                if retVal == 'NOT FOUND':
                    retVal = currentTower.flinks[level].value
                if currentTower == self.start and currentTower.flinks[level].flinks[level] == self.end:
                    self.start.flinks = self.start.flinks[:-1]
                    self.end.flinks = self.end.flinks[:-1]
                currentTower.flinks[level] = currentTower.flinks[level].flinks[level]
            elif key > nextTowerKey:
                currentTower = currentTower.flinks[level]
            else:
                level -= 1
        return retVal

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
    for i in range(10):
        num = random.randrange(100) 
        S.insert(num, 'I found %d' % num)
    print(S)
    keyToSearch = int(input('Enter key to be searched for: '))
    print(S.search(keyToSearch))
    keyToDelete = int(input('Enter key to be deleted for: '))
    print(S.delete(keyToDelete))
    print(S)
