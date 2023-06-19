#!/bin/bash

function writeAndRun {
  PROCESSES=$1
  TRSIZE=$2
  TIMESTAMP=$3
  MODEL=$4
  NODES=$5
  NUM_THREADS=24
  CORES=`python -c "from math import ceil; print int(ceil($PROCESSES/24.0))"`
  cat > $MODEL.sh << EOF
#!/bin/bash

#PBS -l walltime=00:02:00
#PBS -A USNAM37766432
#PBS -l select=$CORES:mpiprocs=1:ncpus=24
#PBS -q debug

export OMP_NUM_THREADS=$NUM_THREADS
cd ~/usna/torch/hdf5/nnScripts/

luajit nnScripts/kernelSelection.lua -traindata /app/projects/usnadeep/data/housenumbers/SVHN_train_hog8.data -testdata /app/projects/usnadeep/data/housenumbers/SVHN_test_hog8.data -gridsearch false -threads $NUM_THREADS -model $MODEL -nfeatures 648 -noutput 10 -numClasses 10 -stdDev 1.511 -nnodes $NODES -nEpochs 1 -trsize .25
EOF
  qsub -e ./output/$MODEL.e -o ./output/$MODEL.o $MODEL.sh
}

PROCESSES=1
TRSIZE=.25
NODES=(100)
MODELS=(cos linear)

TIMESTAMP=`TZ=America/New_York date +%b%d_%H%M_%S`

mkdir $HOME/usna/torch/hdf5/results/$TIMESTAMP
cd $HOME/usna/torch/hdf5/results/$TIMESTAMP
mkdir output

for NODE in "${NODES[@]}"; do 
  for MODEL in "${MODELS[@]}"; do 
    writeAndRun $PROCESSES $TRSIZE $TIMESTAMP $MODEL $NODE
  done
done
