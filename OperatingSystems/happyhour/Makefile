CC	= gcc
CXX	= gcc
CFLAGS	= -O
CXXFLAGS= $(CFLAGS)
LDFLAGS	=

NAME	= new_islander
HDRS	= random_int.h
OBJS	= $(NAME).o random_int.o
LIBS	= -lpthread -lm
OUT	= $(NAME)

$(OUT):	$(OBJS)
	$(CXX) $(LDFLAGS) -o $@ $(OBJS) $(LIBS)

$(NAME).o:	$(NAME).c $(HDRS)
random_int.o:	random_int.c random_int.h

run:
	./new_islander > happyhour.txt

clean:
	/bin/rm -f $(OBJS) *~

clobber:
	/bin/rm -f $(OBJS) $(OUT)
