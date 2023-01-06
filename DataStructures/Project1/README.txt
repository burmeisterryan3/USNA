NAME: MIDN Ryan Burmeister
ASSIGNMENT: Project 1

CLASSES:
For this assignment, I created 5 classes in addition to the supplied classes.
    XMLRepair -
        This is my main class, and it in sense the application of the other
        classes.  This class will print and generate the smallest queue.
    XMLPossibility -
        This class contains a queue and stack which are used to complete one
        complete possibility of the tag scheme.  XMLRepair will compare the
        queue sizes of all the XMLPossibility's to determine which in fact is
        the best option.
    TagStack -
        This class represents the stack used by XMLPossibility.  It contains
        the basic push, pop, peek, and size methods to be used.
    MyQueue -
        This class represents the queue used by XMLPossibility.  It contains
        the basic enqueue, dequeue, peek, and size methods to be used.
    Node -
        This class represents the nodes that will make up any MyQueue that a
        user wishes to develop.
        
COMMENTS:
I tried making my MyQueue class iterable, but I failed miserably in this regard.
I was unsure how to ensure that the iteration was done over every node within the
linked list which made up my queue.  I simply thought that taking the queue and
making a for each loop such as "XMLPossibility xmlp : xmlpQueue" would work, but
this would not create the number of copies necessary which indicated that I was
not iterating through my queue operately.  I left portions of my code commented
out so you could get an idea of what I was trying to do concerning this matter.

Other than trying to figure out why I was coming up with the wrong number of
iteration, the project was not too terribly difficult.  I enjoyed the application
of the knowledge learned in class.

STEPS COMPLETED:
I completed through step 3.5, but did not complete 1.1.

SOURCES:
For help making this class iterable I referenced (did NOT end up using this source):
http://cs-fundamentals.com/data-structures/implement-queue-using-linked-list-in-java.php
