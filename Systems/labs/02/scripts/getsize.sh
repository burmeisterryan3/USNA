#!/bin/bash

if [ -d $1 ]
then
    echo "ERROR: Require file"
elif [ -e $1 ]
then
    ls -l $1 | cut -d " " -f 5
else
    echo "ERROR:  File $1 does not exist" 1>&2
fi
exit