all: fire

fire: fire.c
	mpicc -o fire ./fire.c -lm
run:
	mpirun -n 8 ./fire 100 .8 3 0.333
clean:
	rm -f *~ fire
