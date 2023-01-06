all: life

life: life.o
	gcc life.o -o life

life.o: life.c
	gcc -c life.c

clean:
	rm -f life *.o output.data *~
