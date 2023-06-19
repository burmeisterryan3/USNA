 #!/bin/bash

"""
This script can be used to generate multiple scripts for testing the
swissroll dataset on multiple nodes and CPUs. The results will be 
placed in a subdirectory of the results directory named by the time 
at which this script was run. The purpose of this script was to test
the kernalized neural network against another network in a two class
classification problem.

The tests are run on with multiple settings of standard deveiation
and number of nodes.  Tests will be run on with a cosine activation
function and a hyperbolic tangent activation function, which are set
in the MODEL array to cos and linear respectively.
"""

function writeAndRun {
  CORE=$1
  TIMESTAMP=$2
  MODEL=$3
  NNODE=$4
  SPLIT=$5
  FILENAME=swiss.$MODEL.$NNODE
  NODES=`python -c "from math import ceil; print int(ceil($CORE/24.0))"`
  cat > $FILENAME.sh << EOF
#!/bin/bash

#PBS -l walltime=01:00:00
#PBS -A USNAM37766H58
#PBS -l select=$NODES:mpiprocs=10:ncpus=10
#PBS -q high
cd ~/usna/torch/module/nnScripts/
#PATH="\$HOME/anaconda/bin:\$PATH"
time aprun -n $CORE th kernelSelection.lua -datapath /app/projects/usnadeep/data/swissroll.data -model $MODEL -nnodes $NNODE  -threads 1 -split $SPLIT -nfeatures 3 -noutput 2 -loss nll -learningRate .001 -batchSize 1 -weightDecay 0 -momentum 0 -numClasses 2 -nEpochs 10
EOF
  qsub -e ./output/$FILENAME.e -o ./output/$FILENAME.o $FILENAME.sh
}

CORES=(10)
MODELS=(cos linear)
NNODES=(5)
SPLIT=.75

TIMESTAMP=`TZ=America/New_York date +%b%d_%H%M_%S`

mkdir $HOME/usna/torch/module/results/$TIMESTAMP.swiss
cd $HOME/usna/torch/module/results/$TIMESTAMP.swiss
mkdir output

for CORE in "${CORES[@]}"; do
  for NNODE in "${NNODES[@]}"; do
    writeAndRun $CORE $TIMESTAMP ${MODELS[1]} $NNODE $SPLIT
    writeAndRun $CORE $TIMESTAMP ${MODELS[0]} $NNODE $SPLIT
  done
done
