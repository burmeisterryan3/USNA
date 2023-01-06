import os, sys
import random

names = ['Wolverine', 'Thing', 'Kitty Pryde', 'Magneto', 'Nightcrawler', 'Juggernaut',
'Emma Frost', 'Beast', 'Captain America', 'Spider-Man', 'Puck', 'Alex Wilder',
'Doctor Strange', 'Cyclops', 'Colossus', 'Jubilee', 'Lockheed', 'Nick Fury', 'Nico Minoru',
'Shaman', 'Angel', 'Gambit', 'Hawkeye', 'Iceman', 'Sabretooth', 'Ka-Zar', 'Arcade',
'Karolina Dean', 'Mr. Fantastic', 'Blob', 'Mystique', 'Red Skull', 'Hulk', 'Norman Osborn',
'Silver Samurai', 'Pyro', 'Amadeus Cho', 'The Pride', 'Mockingbird', 'Hercules', 'Lockjaw',
'Chase Stein', 'Molly Hayes', 'Crossbones', 'Armor', 'Northstar', 'Jean Grey', 
'Captain Britain', 'Guardian', 'Scarlet Witch', 'Cannonball', 'Sasquatch', 'Layla Miller',
'Psylocke', 'Wolfsbane', 'Stepford Cuckoos', 'Iron Man', 'Professor X', 'Daredevil',
'Dr. Doom', 'Kingpin', 'Sentinels', 'Invisible Woman', 'Lady Deathstrike', 'Dum Dum Dugan',
'Moonstar', 'Namor', 'X-23', 'Rogue', 'Storm', 'Madrox', 'Dazzler', 'Wiccan', 'Hulkling',
'Patriot', 'Bishop', 'Magik', 'Galactus', 'Banshee', 'Siryn', 'Venom',
'Heather McNeil Hudson', 'Bucky Barnes', 'Quicksilver', 'Elecktra', 'Taskmaster', 'Phobos',
'Dust', 'Apacolypse', 'Humman Torch', 'Stature', 'Thor', 'Cypher', 'Beta Ray Bill',
'Talisman', 'Mr. Sinister', 'Silver Surfer', 'Sunspot', 'Polaris', 'Deadpool']

def mixed():
  m = 128
  a = 5
  c = 21
  order = []
  while len(order) < 8:
    order= [int(os.urandom(4).encode('hex'), 16)]
    for i in range(1,9):
      order.append((a*order[i-1]+c)&(m-1))
    del order[0]
    order = list(set(order))
    for i,num in enumerate(order):
      if num > 99:
        del order[i]
  return [names[i] for i in order] 

def default():
  random.seed()
  order = list(set([random.randint(0,99) for _ in range(8)]))
  while len(order) < 8:
    order = list(set([random.randint(0,99) for _ in range(8)]))
  return [names[i] for i in order] 

def createWinners(order):
  probs = [random.random() for i in range(7)]
  if probs[0] > .5:
    order.append(order[0])
  else:
    order.append(order[1])
  if probs[1] > .5:
    order.append(order[2])
  else:
    order.append(order[3])
  for x in range(2,7):
    if probs[x] > .5:
      order.append(order[2*x])
    else:
      order.append(order[2*x + 1])

  return order    

def printBracket(order):
  n = '\n\n'
  nt1 = '\n\n\t'
  nt2 = '\n\n\t\t'
  nt3 = '\n\n\t\t\t'
  print n+order[0]+nt1+order[8]+n+order[1]+\
        nt2+order[12]+n+order[2]+nt1+order[9]+\
        n+order[3]+nt3+order[14]+n+order[4]+\
        nt1+order[10]+n+order[5]+nt2+order[13]+\
        n+order[6]+nt1+order[11]+n+order[7]+n

def help():
  print '\nArguments for prng include \'mixed\' or \'default\''
  print 'Defalut is python\'s Mersenne Twister\n'
  sys.exit()

if len(sys.argv) < 2 and sys.argv[1] != 'mixed' and sys.argv[1] != 'default':
  help()

prng = sys.argv[1]
if prng == 'default':
  printBracket(createWinners(default()))
else:
  printBracket(createWinners(mixed()))
