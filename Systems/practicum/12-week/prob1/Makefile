#TODO: Complete the MAkefile to compile rpn
#
#  NOTES:
#  (1) you must use clang as your compiler
#  (2) you must compile to .o object files and combine them into an executable
#  (3) you must have a clean to remove binary and .o files
#  (4) you MAY NOT use Makefile vairables, like CC or CFLAGS
#
#  (hint: don't foget about adding in the readline library via -l)


all: rpn



# COMPLETE BLEOW


rpn: rpn.o mathlib.o
	clang -g -lreadline mathlib.o rpn.o -o rpn

clean:
	rm -f run *~