#!/bin/bash

function writeAndRun {
  PROCESSES=$1
  TRSIZE=$2
  TIMESTAMP=$3
  MODEL=$4
  NODES=$5
  STDDEV=$6
  NUM_THREADS=1
  CORES=`python -c "from math import ceil; print int(ceil($PROCESSES/24.0))"`
  cat > $MODEL.sh << EOF
#!/bin/bash

#PBS -l walltime=00:01:00
#PBS -A USNAM37766432
#PBS -l select=$CORES:mpiprocs=1:ncpus=1
#PBS -q standard

export OMP_NUM_THREADS=$NUM_THREADS
cd ~/usna/torch/hdf5/nnScripts/

luajit kernelSelection.lua -traindata /app/projects/usnadeep/data/housenumbers/SVHN_train_hog8.data -testdata /app/projects/usnadeep/data/housenumbers/SVHN_test_hog8.data -extradata /app/projects/usnadeep/data/housenumbers/SVHN_extra_hog8.data -threads $NUM_THREADS -model $MODEL -nfeatures 648 -noutput 10 -numClasses 10 -stdDev $STDDEV -nnodes $NODES -nEpochs 200 -trsize .25
EOF
  qsub -e ./output/$MODEL.e -o ./output/$MODEL.o $MODEL.sh
}

PROCESSES=1
TRSIZE=.25
NODES=(1247 1330)
MODELS=(cos linear)
STDDEV=1.7332

TIMESTAMP=`TZ=America/New_York date +%b%d_%H%M_%S`

mkdir $HOME/usna/torch/hdf5/results/$TIMESTAMP
cd $HOME/usna/torch/hdf5/results/$TIMESTAMP
mkdir output

writeAndRun $PROCESSES $TRSIZE $TIMESTAMP ${MODELS[0]} ${NODES[0]} $STDDEV
writeAndRun $PROCESSES $TRSIZE $TIMESTAMP ${MODELS[1]} ${NODES[1]} $STDDEV
