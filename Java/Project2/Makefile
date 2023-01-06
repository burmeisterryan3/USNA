.SUFFIXES: .java .class

.java.class:
	javac $*.java

CLASSES = Location.class \
					Board.class \
					Chess.class

all: classes compile
	javadoc -d doc *.java

classes: $(CLASSES:.java=.class)

compile:
	javac *.java

run:
	java Chess

clean : 
	rm *.class
	rm -rf ./doc/*.html ./doc/package-list ./doc/resources/ ./doc/stylesheet.css
