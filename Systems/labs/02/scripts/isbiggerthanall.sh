#!/bin/bash

if [ -z $1 ] || [ -z $2 ]
then
    echo "$0:  ERROR:  Require a size and at least one file"
fi
for arg in $*
do
    ./isbiggerthan.sh $1 $arg >/dev/null
    case $? in
    0 )
        echo $arg
        ;;
    esac
done
exit

