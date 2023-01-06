#!/bin/bash

#TODO: Add relevent link clean up commands
unlink b
ln -s a b
unlink c
ln -s a c
unlink d
ln -s a d
unlink k
ln -s e k

unlink dir1/f
ln -s ../a dir1/f

unlink dir2/g
ln -s ../a dir2/g

unlink dir3/j
ln -s i dir3/j