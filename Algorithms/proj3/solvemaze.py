#!/usr/bin/python3

''' SI 335 Spring 2015
    Project 3
    YOUR NAME HERE
'''

import re
import random
import sys

def calcMoves(maze):
    '''This function takes in a 2D-array that stores a maze description,
       and returns a list of "moves" to make in order to solve the maze.
    '''
    # THIS IS THE FUNCTION YOU NEED TO RE-WRITE
    # Width and height calculated from maze size, minus borders
    w = len(maze) - 2
    h = len(maze[0]) - 2
    startPos = (1, 0)
    endPos = (h, w+1)
    curY, curX = startPos

    # The solution here is terrible: it just randomly mills about until
    # it happens upon the end or it runs out of points.
    moves = []
    while len(moves) < 4*h*w:
        nextY, nextX = curY, curX
        # randomly choose a direction
        if random.randrange(0,2) == 0:
            # go left/right
            if random.randrange(0,2) == 0:
                nextX -= 1
                nextMove = 'L'
            else:
                nextX += 1
                nextMove = 'R'
        else:
            # go up/down
            if random.randrange(0,2) == 0:
                nextY -= 1
                nextMove = 'U'
            else:
                nextY += 1
                nextMove = 'D'
        if nextX < 0 or nextX > w+1 or maze[nextY][nextX] == 'X':
            # illegal move; give up and try another.
            continue
        moves.append(nextMove)
        curY, curX = nextY, nextX

    return moves


def readMaze(stream):
    '''This functions reads in a maze description from the given file
       and returns a 2-dimensional array of characters.
       ' ' means a space in the maze,
       'X' means an impenetrable wall, and
       'O' means a lucrative prize.
    '''
    # YOU DON'T NEED TO CHANGE THIS FUNCTION.
    notAllowed = re.compile('[^XO ]')
    width = 0
    maze = []
    prizes = set()
    rowind = 0
    for line in stream:
        line = line.rstrip()
        s = notAllowed.search(line)
        if s:
            raise ValueError('Illegal character in maze: {}'.format(s.group(0)))
        row = list(line)
        wdiff = width - len(row)
        if wdiff > 0:
            row.extend([' '] * wdiff)
        elif wdiff < 0:
            for otherRow in maze:
                otherRow.extend([' '] * (-wdiff))
            width = len(row)
        maze.append(row)
        rowind += 1
    assert len(maze) >= 2
    return tuple(tuple(row) for row in maze)

def writeMoves(moves):
    '''Writes the list of moves to standard out'''
    # YOU DON'T NEED TO CHANGE THIS FUNCTION
    for move in moves:
        print(move)

if __name__ == '__main__':
    # THIS IS THE MAIN METHOD. YOU DON'T NEED TO CHANGE IT.
    maze = readMaze(sys.stdin)
    moves = calcMoves(maze)
    writeMoves(moves)
