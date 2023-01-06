all: executable

executable: library.o fun.o
	clang -g -Wall library.o runme.c -o runme
	clang -g -Wall library.o fun.o dontrunme.c -o dontrunme

library.o: library.c library.h
	clang -c library.c -o library.o

fun.o: fun.c fun.h
	clang -c fun.c -o fun.o

runme: library.o
	clang -g -Wall library.o runme.c -o runme

dontrunme: library.o fun.o
	clang -g -Wall library.o fun.o dontrunme.c -o dontrunme

run:
	./runme

dontrun:
	./dontrunme

clean:
	rm -f *.o
	rm -f runme
	rm -f dontrunme