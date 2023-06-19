#!/bin/bash

"""
This script can be used to generate multiple scripts for testing the
mnist dataset on multiple nodes and CPUs. The results will be 
placed in a subdirectory of the results directory named by the time 
at which this script was run. The tests are run on with multiple
settings of standard deveiation and number of nodes.  Tests will be
run on with a cosine activation function and a hyperbolic tangent
activation function, which are set in the MODEL array to cos and linear
respectively.

The script takes one argument which should specifiy the name of the
data file within the group unsadeep/data directory (e.g. mnist.pca). 

The purpose of this file was to produce results that could be compared
between the new kernalized nerual network and a more traditional 
network. However, the results of the kernalized version did not produce
beneficial results, so the runStdDev.sh script was created.  This file
should only be run after an adequate range of standard deviations has 
been determined.
"""

function writeAndRun {
  PROCESSES=$1
  TIMESTAMP=$2
  MODEL=$3
  NNODE=$4
  LBOUND=$5
  UBOUND=$6
  SPLIT=$7
  FILENAME=mnist.$NNODE
  CORES=`python -c "from math import ceil; print int(ceil($PROCESSES/24.0))"`
  cat > $FILENAME.sh << EOF
#!/bin/bash

#PBS -l walltime=00:30:00
#PBS -A USNAM37766H58
#PBS -l select=$CORES:mpiprocs=24:ncpus=24
#PBS -q high
cd ~/usna/torch/module/nnScripts/
#PATH="\$HOME/anaconda/bin:\$PATH"
aprun -n $PROCESSES th kernelSelection.lua -datapath /app/projects/usnadeep/data/mnistdata -model $MODEL -nnodes $NNODE -threads 1 -split $SPLIT -nfeatures 784 -noutput 10 -loss nll -learningRate .001 -batchSize 1 -weightDecay 0 -momentum 0 -numClasses 10 -nEpochs 10 -lbound $LBOUND -ubound $UBOUND
EOF
  qsub -e ./output/$FILENAME.e -o ./output/$FILENAME.o $FILENAME.sh
}

PROCESSES=480
MODEL=cos
STDDEV=(.25 .5 .75 1 2)
NNODES=(50 100 200 400)
SPLIT=.75

TIMESTAMP=`TZ=America/New_York date +%b%d_%H%M_%S`

mkdir $HOME/usna/torch/module/results/$TIMESTAMP.mnist
cd $HOME/usna/torch/module/results/$TIMESTAMP.mnist
mkdir output

COUNTER=0
LENGTH=${#STDDEV[@]}
let LENGTH=LENGTH-1
while [ $COUNTER -lt $LENGTH ]; do
  for NODES in "${NNODES[@]}"; do
    writeAndRun $PROCESSES $TIMESTAMP $MODEL $NODES ${STDDEV[$COUNTER+1]} ${STDDEV[$COUNTER+1]} $SPLIT
  done
  let COUNTER=COUNTER+1
done
