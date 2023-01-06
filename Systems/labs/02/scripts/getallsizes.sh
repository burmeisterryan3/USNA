#!/bin/bash

for arg in $*
do
    if [ -e $arg ]
    then
	echo "$arg" | xargs echo -n
	ls -l $arg | cut -d " " -f 5 | xargs echo ""
    else
	echo "$0:  ERROR:  File $arg does not exit"
    fi
done
exit
