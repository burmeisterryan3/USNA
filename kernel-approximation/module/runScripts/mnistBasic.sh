#!/bin/bash

#PBS -l walltime=02:00:00
#PBS -A USNAM37766H58
#PBS -l select=1:ncpus=10:mpiprocs=10
#PBS -q high
#PBS -N noCuda
cd ~/usna/torch/module/nnScripts/
time aprun -n 1 th doall.lua -datapath $HOME/usna/torch/module/data/mnist.data -model linear -nnodes 50 -mnist true -threads 10 -split .75 -nfeatures 784 -noutput 10 -loss nll -learningRate .001 -batchSize 1 -weightDecay 0 -momentum 0 -numClasses 10 -nEpochs 20 -type double
