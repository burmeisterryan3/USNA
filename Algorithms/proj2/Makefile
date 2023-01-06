# Makefile for SI 335 project 2

CPPFLAGS=-O3 -Wall -Wno-sign-compare -Wno-unused-function -g

rsa: rsa.o posint.o
	g++ $(CPPFLAGS) -o $@ $^

%.o: %.cpp posint.hpp
	g++ $(CPPFLAGS) -c $<

generate:
	./rsa -k pubkey.txt privkey.txt

encrypt:
	./rsa -e pubkey.txt > encrypted.txt

decrypt:
	./rsa -d privkey.txt < encrypted.txt

.PHONY: clean

clean:
	rm -f *.o rsa
