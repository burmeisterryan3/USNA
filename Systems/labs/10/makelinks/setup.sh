#!/bin/bash

touch a b c d e
mkdir dir1/ dir2/ dir3/

#TODO: Add relevant linking commands
ln -s a s
ln -s s t

ln a dir1/a
ln a dir2/a
ln a dir3/a

cd dir3/
ln -s ../c c
ln -s ../d d
ln -s ../t gonavy