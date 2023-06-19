#!/bin/bash

function writeAndRun {
  PROCESSES=$1
  TRSIZE=$2
  TIMESTAMP=$3
  MODEL=$4
  NODES=$5
  CORES=`python -c "from math import ceil; print int(ceil($PROCESSES/24.0))"`
  cat > $MODEL.sh << EOF
#!/bin/bash

#PBS -l walltime=00:30:00
#PBS -A USNAM37766432
#PBS -l select=$CORES:mpiprocs=24:ncpus=24
#PBS -q debug
cd ~/usna/torch/hdf5/nnScripts/

aprun -n $PROCESSES luajit kernelSelection.lua -traindata /app/projects/usnadeep/data/housenumbers/SVHN_train_hog8.data -testdata /app/projects/usnadeep/data/housenumbers/SVHN_test_hog8.data -gridsearch true -model $MODEL -nfeatures 648 -noutput 10 -numClasses 10 -lowStd 1 -uprStd 2 -lowNodes 500 -uprNodes 2000 -nEpochs 1 -trsize $TRSIZE
EOF
  qsub -e ./output/$MODEL.e -o ./output/$MODEL.o $MODEL.sh
}

PROCESSES=24
TRSIZE=.25
NODES=(100 200 300 400 500 600 700 800 900 1000 1100 1200 1300 1400 1500 1600 1700 1800 1900 2000)
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