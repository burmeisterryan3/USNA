all: heat
setup:
	mkdir log/ 
remove:
	rm -f ./log/*
heat: heatex.c
	gcc -g -o heatex ./heatex.c
run:
	./heatex 5 && ./runPython.sh
clean:
	rm -f *~ fire output* ./log/*
