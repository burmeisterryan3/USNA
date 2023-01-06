#!/bin/bash

if [ -z $1 ] || [ -z $2 ]
then
    echo "$0:  Error:  Require path and size"
    exit 2
elif [ ! -e $2 ]
then
    echo "$0:  ERROR:  File $2 does not exist"
    exit 5
else
    file_size=$(ls -l $2 | cut -d " " -f 5)
    if [ "$1" -eq "$1" ] 2> /dev/null #ensure that it is number
    then
	if [ $1 -lt 0 ]
	then
            echo "$0:  ERROR:  Require a positive number for $1"
	    exit 4
	elif [ $file_size -gt $1 ] 2> /dev/null
	then
            echo "yes"
	    exit 0
	else
            echo "no"
	    exit 1
	fi
    else
        "$0:  ERROR:  Require a number for $1"
	exit 3
    fi
fi
exit