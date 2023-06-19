#!/bin/bash

#PBS -l walltime=00:10:00
#PBS -A USNAM37766H58
#PBS -l select=10:mpiprocs=24:ncpus=24
#PBS -q high
cd ~/usna/torch/module
aprun -n 240 th kernelSelection.lua -model cos -nnodes 20 -datapath $HOME/usna/torch/module/data/swissroll.data -split .67 -nfeatures 3 -noutput 2 -loss nll -learningRate .001 -batchSize 1 -weightDecay 0 -momentum 0 -numClasses 2 -nEpochs 10 -lbound 0.0001 -ubound 5
