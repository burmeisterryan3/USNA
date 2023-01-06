#!/bin/bash

name=$1
grep $name /etc/passwd | cut -d ":" -f 5
exit