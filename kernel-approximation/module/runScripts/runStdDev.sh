#!/bin/bash

#This script can be used to generate multiple scripts for testing the
#mnist dataset on multiple nodes and CPUs. The results will be 
#placed in a subdirectory of the results directory named by the time 
#at which this script was run. The tests are run on with multiple
#settings of standard deveiation and number of nodes.  Tests will be
#run on with a cosine activation function and a hyperbolic tangent
#activation function, which are set in the MODEL array to cos and linear
#respectively.
#
#The script takes one argument which should specifiy the name of the
#data file within the group unsadeep/data directory (e.g. mnist.pca). 
#
#The purpose of this file was to attempt to determine which setting
#of standard deviation was best for the intialization of the distribution
#for which the weights were drawn in the kernalized neural net.  The
#tests are still not giving accuracy percentages above 11%.  The next
#step has been to use the convert.lua file to generate preprocessed data.

function writeAndRun {
  CORE=$1
  TIMESTAMP=$2
  MODEL=$3
  NNEURON=$4
  SPLIT=$5
  DATA=$6
  FILENAME=stddev.$MODEL.$STDDEV.$NNEURON
  NODES=`python -c "from math import ceil; print int(ceil($CORE/24.0))"`
  cat > $FILENAME.sh << EOF
#!/bin/bash

#PBS -l walltime=05:00:00
#PBS -A USNAM37766H58
#PBS -l select=$NODES:mpiprocs=24:ncpus=24
#PBS -q high
cd ~/usna/torch/module/nnScripts/
#PATH="\$home/anaconda/bin:\$PATH"
time aprun -n $CORE th kernelSelection.lua -datapath /app/projects/usnadeep/data/$DATA -model $MODEL -nnodes $NNEURON -mnist true -threads 1 -split $SPLIT -nfeatures 784 -noutput 10 -loss nll -learningRate .001 -batchSize 1 -weightDecay 0 -momentum 0 -numClasses 10 -nEpochs 3 -lbound 10 -ubound 200
EOF
  qsub -e ./output/$FILENAME.e -o ./output/$FILENAME.o $FILENAME.sh
}

CORE=2000
MODEL=cos
NNEURON=20
SPLIT=.75
DATA=$1

TIMESTAMP=`TZ=America/New_York date +%b%d_%H%M_%S`

mkdir $HOME/usna/torch/module/results/$TIMESTAMP.stddev
cd $HOME/usna/torch/module/results/$TIMESTAMP.stddev
mkdir output

writeAndRun $CORE $TIMESTAMP $MODEL $NNEURON $SPLIT $DATA
