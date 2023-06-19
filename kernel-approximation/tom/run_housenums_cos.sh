#!/bin/bash

#PBS -l walltime=5:00:00
#PBS -A USNAM37766H58
#PBS -l select=1:mpiprocs=1:ncpus=4
#PBS -q high
#PBS -e ./output/house_cos_errG.txt
#PBS -o ./output/house_cos_outG.txt

cd ~/usna/torch/tom
aprun -n 1 -N 1 th train_housenumbers_wrapper.lua -threads 4 -maxIter 1 -useKernel -kernelStd 5
