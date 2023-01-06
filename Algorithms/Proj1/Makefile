all:
	g++ -O6 -o sel sel.cpp
filetime:
	/usr/bin/time -v ./sel < in.txt > out.txt
file:
	./sel < in.txt > out.txt
time:
	/usr/bin/time -v ./sel < in.txt > /dev/null
clean:
	rm -f *~ out.txt sel
