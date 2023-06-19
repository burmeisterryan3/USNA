#!/bin/bash

#PBS -l walltime=5:00:00
#PBS -A USNAM37766H58
#PBS -l select=83:mpiprocs=24:ncpus=24
#PBS -q high
#PBS -e ./output/house_cos_errG.txt
#PBS -o ./output/house_cos_outG.txt

cd ~/usna/torch/tom
aprun -n 1992 th kernelSelection.lua -maxIter 5 -useKernel -lbound .001 -ubound 1
#aprun -n 24 th kernelSelection.lua -maxIter 1 -useKernel -lbound .001 -ubound 4
