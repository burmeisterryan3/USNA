"""
MIDN Burmeister
CDR Bilzor, Operating Systems
Programming Assignment 2, Scheduling Algorithms
"""

# Maintain the necessary information for each job
class Job():
  def __init__(self, pid, arriveTime, serviceTime, priority):
    self.pid = pid
    self.arriveTime = arriveTime
    self.serviceTime = serviceTime
    self.priority = priority
    self.waitTime = 0
    self.startTime = -1
    self.completeTime = 0
    self.processTime = 0

# Read the file and store in a list
def readFile(jobs):
  with open('jobdata.txt') as f:
    for line in f:  
      y = [int(x) for x in line.split()]
      jobs.append(Job(y[0], y[1], y[2], y[3]))

# Reset the information to ensure that we can use next algorithm
def reset(jobs):
  for job in jobs:
    job.waitTime = 0
    job.startTime = -1
    job.completeTime = 0
    job.processTime = 0

# Calculate the efficiency times and print them
def efficiency(jobs, numJobs, runningTime):
  totTimeResponse = 0
  totTimeTurnaround = 0
  for job in jobs:
    totTimeResponse = totTimeResponse + \
                      job.startTime - job.arriveTime
    totTimeTurnaround = totTimeTurnaround + \
                        job.completeTime - job.arriveTime
  avgTimeRespone = float(totTimeResponse) / numJobs
  avgTimeTurnaround = float(totTimeTurnaround) / numJobs
  avgThroughput = numJobs / float(runningTime)
  print "Average response time: ", avgTimeRespone
  print "Average turnaround time: ", avgTimeTurnaround
  print "Average throughput: ", avgThroughput

# Print efficiencies and reset necessary information
def printAndReset(jobs, numJobs, runningTime):
  efficiency(jobs, numJobs, runningTime)
  reset(jobs)
  print "\n\n"

# For RR, insert into correct position in list for new job
def insert(running, job):
  if not running:
    running.append(job)
  else:
    temp = running[len(running)-1]
    running[len(running)-1] = job
    running.append(temp)

# For RR, shift after a quantum is completed
def shift(running):
  if len(running) > 1:
    newRunning = []
    temp = running[0]
    for i in range(1, len(running)):
      newRunning.append(running[i])
    newRunning.append(temp)
    return newRunning
  else:
    return running

# Perform a swap to get necessary process first in list
def swap(running, index):
  temp = running[index]
  running[index] = running[0]
  running[0] = temp

# Ensure max priority is at front of list
def maxPriorityFirst(running):
  maxPriority = 0
  length = len(running)
  for i in range(1, length):
    if running[i].priority < running[maxPriority].priority:
      maxPriority = i 
  swap(running, maxPriority) 

# Ensure shortest process is at front of list
def shortestProcessFirst(running):
  shortest = 0
  length = len(running)
  for i in range(1, length):
    if running[i].serviceTime < running[shortest].serviceTime:
      shortest = i
  swap(running, shortest) 

# Ensure shortest remaining time process is at front of list
def srtFirst(running):
  shortest = 0
  length = len(running)
  for i in range(1, length):
    shortestDiff = running[shortest].serviceTime - \
                   running[shortest].processTime
    iDiff = running[i].serviceTime - running[i].processTime
    if iDiff < shortestDiff:
      shortest = i
    # Not necessary, but for debugging matches book
    elif iDiff == shortestDiff and\
         running[i].pid < running[shortest].pid:
       shortest = i 
  swap(running, shortest)

# Ensure that the process with the hrrn is at front of list
def hrrnFirst(running):
  highest = 0
  length = len(running)
  for i in range(1, length):
    highestDiff = (running[highest].waitTime +\
                   running[highest].serviceTime) /\
                   running[highest].serviceTime
    iDiff = (running[i].waitTime + running[i].serviceTime) /\
             running[i].serviceTime
    if iDiff > highestDiff:
      highest = i
  swap(running, highest)

# Perform RR algorithm with time-slice/quantum of 3
def roundRobin(jobs, numJobs):
  quantum = 3
  timeInQuantum = 0
  running = []
  runningTime = 0
  nextJob = 0
  justDeleted = False
  timeOut = False
 
  while True:
    # Add new process if necessary, and protect from segfault
    if nextJob < numJobs and runningTime == jobs[nextJob].arriveTime:
      # Add to back if process just ended or not at end of quantum
      if justDeleted or not timeOut:
        running.append(jobs[nextJob])
      else:
        # Add one from back if shift occurred due to end of quantum
        insert(running, jobs[nextJob])
      nextJob = nextJob + 1
    if running:
      # Reset these values
      justDeleted = False
      timeOut = False
      # If process has not been run yet, set start time
      if running[0].startTime == -1:
        running[0].startTime = runningTime
      running[0].processTime = running[0].processTime + 1 
      timeInQuantum = timeInQuantum + 1
      # If process has finished, remove it form list
      if running[0].processTime == running[0].serviceTime:
        running[0].completeTime = runningTime
        timeInQuantum = 0
        del running[0]
        justDeleted = True
      # Shift list if quantum is up for a process
      elif timeInQuantum == quantum:
        running = shift(running)
        timeInQuantum = 0
        timeOut = True
    # If not all processes have run, set valus appropriately
    else:
      timeInQuantum = 0
      justDeleted = False
      timeOut = False
    # If we are done, break loop
    if not running and nextJob == numJobs:
      break
    else:
      runningTime = runningTime + 1 

  print "---------ROUND ROBIN-----------" 
  printAndReset(jobs, numJobs, runningTime)

# Perform first-come, first-serve algorithm
def fcfs(jobs, numJobs):
  running = []
  timeRunning = 0
  nextJob = 0
  while True:
    # Add new process if necessary, and protect from segfault
    if nextJob < numJobs and timeRunning == jobs[nextJob].arriveTime:
      running.append(jobs[nextJob])
      jobs[nextJob].arriveTime = timeRunning
      nextJob = nextJob + 1
    if running:
      # If process has not been run yet, set start time
      if running[0].startTime == -1:
        running[0].startTime = timeRunning
      running[0].processTime = running[0].processTime + 1
      # If process has finished, remove it form list
      if running[0].processTime == running[0].serviceTime:
        running[0].completeTime = timeRunning
        del running[0]
    # If we are done, break loop
    if not running and nextJob == numJobs:
      break
    else:
      timeRunning = timeRunning + 1
 
  print "---------FIRST COME FIRST SERVE-----------" 
  printAndReset(jobs, numJobs, timeRunning)

# Perform priority algorithm
def priority(jobs, numJobs):
  nextJob = 0
  runningTime = 0
  running = []
 
  while True:
    # Add new process if necessary, and protect from segfault
    if nextJob < numJobs and runningTime == jobs[nextJob].arriveTime:
      running.append(jobs[nextJob])
      jobs[nextJob].arriveTime = runningTime
      nextJob = nextJob + 1
      # Preemptive
      maxPriorityFirst(running)           
    if running:
      # If process has not been run yet, set start time
      if running[0].startTime == -1:
        running[0].startTime = runningTime
      running[0].processTime = running[0].processTime + 1
      # If process has finished, remove it form list
      if running[0].processTime == running[0].serviceTime:
        running[0].completeTime = runningTime
        del running[0]
    # If we are done, break loop
    if not running and nextJob == numJobs:
      break
    else:
      runningTime = runningTime + 1
 
  print "---------PRIORITY-----------" 
  printAndReset(jobs, numJobs, runningTime)

# Perform shortest process next algorithm
def spn(jobs, numJobs):
  nextJob = 0
  runningTime = 0
  running = []
 
  while True:
    # Add new process if necessary, and protect from segfault
    if nextJob < numJobs and runningTime == jobs[nextJob].arriveTime:
      running.append(jobs[nextJob])
      jobs[nextJob].arriveTime = runningTime
      nextJob = nextJob + 1
    if running:
      # If process has not been run yet, set start time
      if running[0].startTime == -1:
        running[0].startTime = runningTime
      running[0].processTime = running[0].processTime + 1
      # If process has finished, remove it form list
      if running[0].processTime == running[0].serviceTime:
        running[0].completeTime = runningTime
        del running[0]
        # Fine new process to run
        if running:
          shortestProcessFirst(running)           
    # If we are done, break loop
    if not running and nextJob == numJobs:
      break
    else:
      runningTime = runningTime + 1
 
  print "---------SHORTEST PROCESS NEXT-----------"
  printAndReset(jobs, numJobs, runningTime)

# Perform shortest remaining time algorithm
def srt(jobs, numJobs):
  nextJob = 0
  runningTime = 0
  running = []
 
  while True:
    # Add new process if necessary, and protect from segfault
    if nextJob < numJobs and runningTime == jobs[nextJob].arriveTime:
      running.append(jobs[nextJob])
      jobs[nextJob].arriveTime = runningTime
      nextJob = nextJob + 1
      # Preemptive
      srtFirst(running)           
    if running:
      # If process has not been run yet, set start time
      if running[0].startTime == -1:
        running[0].startTime = runningTime
      running[0].processTime = running[0].processTime + 1
      # If process has finished, remove it form list
      if running[0].processTime == running[0].serviceTime:
        running[0].completeTime = runningTime
        del running[0]
        # To ensure that the lower pid number runs first
        if len(running) != 0:
          srtFirst(running)
    # If we are done, break loop
    if not running and nextJob == numJobs:
      break
    else:
      runningTime = runningTime + 1
 
  print "---------SHORTEST REMAINING TIME-----------" 
  printAndReset(jobs, numJobs, runningTime)

# Perform highest response ratio algorithm next
def hrrn(jobs, numJobs):
  nextJob = 0
  runningTime = 0
  running = []
  while True:
    # Add new process if necessary, and protect from segfault
    if nextJob < numJobs and runningTime == jobs[nextJob].arriveTime:
      running.append(jobs[nextJob])
      jobs[nextJob].arriveTime = runningTime
      nextJob = nextJob + 1
    if running:
      # If process has not been run yet, set start time
      if running[0].startTime == -1:
        running[0].startTime = runningTime
      running[0].processTime = running[0].processTime + 1
      # Update wait times
      for i in range(1, len(running)):
        running[i].waitTime = running[i].waitTime + 1 
      # If process has finished, remove it form list
      if running[0].processTime == running[0].serviceTime:
        running[0].completeTime = runningTime
        del running[0]
        # Find new process to run
        if running:
          hrrnFirst(running)
    # If we are done, break loop
    if not running and nextJob == numJobs:
      break
    else:
      runningTime = runningTime + 1 
 
  print "---------HIGHEST RESPONSE RATIO-----------" 
  printAndReset(jobs, numJobs, runningTime)

# Run all of the algorithms
if __name__ == "__main__":
  jobs = [];
  readFile(jobs)
  numJobs = len(jobs)
  roundRobin(jobs, numJobs)
  priority(jobs, numJobs)
  fcfs(jobs, numJobs)
  spn(jobs, numJobs)
  srt(jobs, numJobs)
  hrrn(jobs, numJobs)
