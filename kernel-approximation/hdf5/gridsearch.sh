#!/bin/bash

function writeAndRunCos {
  PROCESSES=$1
  TRSIZE=$2
  TIMESTAMP=$3
  MODEL=$4
  LOWNODES=$5
  UPRNODES=$6
  LOWDEV=$7
  UPRDEV=$8
  LOWLEARN=$9
  UPRLEARN=${10}
  LOWDECAY=${11}
  UPRDECAY=${12}
  CORES=`python -c "from math import ceil; print int(ceil($PROCESSES/24.0))"`
  cat > $MODEL.sh << EOF
#!/bin/bash

# change walltime appropriately
#PBS -l walltime=20:00:00
#PBS -A USNAM37766432
#PBS -l select=$CORES:mpiprocs=24:ncpus=24
#PBS -q standard
cd ~/usna/torch/hdf5/nnScripts/

aprun -n $PROCESSES luajit kernelSelection.lua -traindata /app/projects/usnadeep/data/housenumbers/SVHN_train_hog8.data -testdata /app/projects/usnadeep/data/housenumbers/SVHN_test_hog8.data -gridStdDev -gridNode -gridLearnRate -gridWeightDecay -model $MODEL -nfeatures 648 -noutput 10 -numClasses 10 -lowStd $LOWDEV -uprStd $UPRDEV -lowNodes $LOWNODES -uprNodes $UPRNODES -lowLearn $LOWLEARN -uprLearn $UPRLEARN -lowDecay $LOWDECAY -uprDecay $UPRDECAY -nEpochs 5 -trsize $TRSIZE
EOF
  qsub -e ./output/$MODEL.e -o ./output/$MODEL.o $MODEL.sh
}

function writeAndRunTanh {
  PROCESSES=$1
  TRSIZE=$2
  TIMESTAMP=$3
  MODEL=$4
  LOWNODES=$5
  UPRNODES=$6
  LOWLEARN=$7
  UPRLEARN=$8
  LOWDECAY=$9
  UPRDECAY=${10}
  CORES=`python -c "from math import ceil; print int(ceil($PROCESSES/24.0))"`
  cat > $MODEL.sh << EOF
#!/bin/bash

# change walltime appropriately
#PBS -l walltime=20:00:00
#PBS -A USNAM37766432
#PBS -l select=$CORES:mpiprocs=24:ncpus=24
#PBS -q debug
cd ~/usna/torch/hdf5/nnScripts/

aprun -n $PROCESSES luajit kernelSelection.lua -traindata /app/projects/usnadeep/data/housenumbers/SVHN_train_hog8.data -testdata /app/projects/usnadeep/data/housenumbers/SVHN_test_hog8.data -gridNode -gridLearnRate -gridWeightDecay -model $MODEL -nfeatures 648 -noutput 10 -numClasses 10 -lowNodes $LOWNODES -uprNodes $UPRNODES -lowLearn $LOWLEARN -uprLearn $UPRLEARN -lowDecay $LOWDECAY -uprDecay $UPRDECAY -nEpochs 5 -trsize $TRSIZE
EOF
  qsub -e ./output/$MODEL.e -o ./output/$MODEL.o $MODEL.sh
}

PROCESSES=24
TRSIZE=.25
NODES=(25 49) #change to 1000 1800
STDDEV=(1. 1.75) #change to .5
LEARNRATE=(.001 .0001) #change to .001 and .00001
WEIGHTDECAY=(.0001 .0001) #change to 0 and .0001
MODELS=(cos linear)

TIMESTAMP=`TZ=America/New_York date +%b%d_%H%M_%S`

mkdir $HOME/usna/torch/hdf5/results/$TIMESTAMP
cd $HOME/usna/torch/hdf5/results/$TIMESTAMP
mkdir output

writeAndRunCos $PROCESSES $TRSIZE $TIMESTAMP ${MODELS[0]} ${NODES[0]} ${NODES[1]} ${STDDEV[0]} ${STDDEV[1]} ${LEARNRATE[0]} ${LEARNRATE[1]} ${WEIGHTDECAY[0]} ${WEIGHTDECAY[1]}

writeAndRunTanh $PROCESSES $TRSIZE $TIMESTAMP ${MODELS[1]} ${NODES[0]} ${NODES[1]} ${LEARNRATE[0]} ${LEARNRATE[1]} ${WEIGHTDECAY[0]} ${WEIGHTDECAY[1]}
