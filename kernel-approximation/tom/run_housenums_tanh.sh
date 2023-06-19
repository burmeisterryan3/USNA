#!/bin/bash

#PBS -l walltime=5:00:00
#PBS -A USNAM37766H58
#PBS -l select=1:mpiprocs=1:ncpus=4
#PBS -q high
#PBS -e ./house_tanh_err.txt
#PBS -o ./house_tanh_out.txt

cd ~/versioned/usna/torch/tom
aprun -n 1 -N 1 th train_housenumbers.lua -threads 4 -maxIter 10 
