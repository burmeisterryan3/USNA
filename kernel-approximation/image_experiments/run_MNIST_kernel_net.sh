#!/bin/bash

#PBS -l walltime=5:00:00
#PBS -A USNAM37766H58
#PBS -l select=1:mpiprocs=1:ncpus=4
#PBS -q high
#PBS -e ./output/house_cos_err.txt
#PBS -o ./output/house_cos_out.txt

cd ~/versioned/usna/torch/tom
aprun -n 1 -N 1 th doall.lua  -model mlp_kernel  -dataset MNIST  -threads 4  
