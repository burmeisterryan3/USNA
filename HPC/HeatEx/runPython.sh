#!/bin/bash

com="python color.py "
disp="eog ./log/output00000.png"
for fn in log/output*.csv
do
  $com$fn
done
$disp
