#!/bin/bash

"""
This script can be used to generate multiple scripts for testing the
swissroll dataset on GPUs. It is important to note the number of CPUs
and GPUs on the machine you will be running this script.  As of right
now, the script is set to run on Shepard where there are 10 CPUs and 1
GPU per GPU node.  The results will be placed in a subdirectory of the
results directory named by the time at which this script was run.
"""

function writeAndRun {
  CORE=$1
  TIMESTAMP=$2
  MODEL=$3
  STDDEV=$4
  NNODE=$5
  FILENAME=swissgpu.$MODEL.$STDDEV.$NNODE
  cat > $FILENAME.sh << EOF
#!/bin/bash

#PBS -l walltime=00:30:00
#PBS -A USNAM37766H58
#PBS -l select=$CORE:ncpus=10:accelerator_model=Tesla_K40s
#PBS -q gpu
cd ~/usna/torch/module/nnScripts/
#PATH="\$HOME/anaconda/bin:\$PATH"
time aprun -n $CORE th doall.lua -type cuda -datapath /app/projects/usnadeep/data/swissroll.data -stdDev $STDDEV -model $MODEL -nnodes $NNODE
EOF
  qsub -e ./output/$FILENAME.e -o ./output/$FILENAME.o $FILENAME.sh
}

CORES=(1)
MODELS=(cos linear)
STDDEVS=(.75)
NNODES=(10)

TIMESTAMP=`TZ=America/New_York date +%b%d_%H%M_%S`

mkdir results/$TIMESTAMP.swissgpu
cd results/$TIMESTAMP.swissgpu
mkdir output

for CORE in "${CORES[@]}"; do
  for NNODE in "${NNODES[@]}"; do
    writeAndRun $CORE $TIMESTAMP ${MODELS[1]} 1 $NNODE
    for STDDEV in "${STDDEVS[@]}"; do
      writeAndRun $CORE $TIMESTAMP ${MODELS[0]} $STDDEV $NNODE
    done
  done
done
