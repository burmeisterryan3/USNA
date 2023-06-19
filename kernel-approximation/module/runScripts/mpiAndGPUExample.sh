#!/bin/bash
#
#PBS -A USNAM37766H58
#PBS -l select=2:ncpus=10:accelerator_model=Tesla_K40s
#PBS -l walltime=00:05:00
#PBS -N gpuMPItest
#PBS -q gpu
#PBS -j oe
#
###############################
#
# This script requests two nodes, each with 10 cpus and 1 gpu.
#
# The aprun command says run 2 mpi instances (-n) of the command, with 1 per
# node (-N)
#
###############################
cd $PBS_O_WORKDIR
#
export MPICH_RDMA_ENABLED_CUDA=1
#
aprun -n 2 -N 1 th mpiDoall.lua -model linear -type cuda -nEpochs 2 -datapath /p/home/gvtaylor/usna/torch/swissroll
#
exit
