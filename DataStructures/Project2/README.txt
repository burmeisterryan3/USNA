MIDN Burmeister
160786

BoggleHashMap class-
	For this map I decided to use a hashtable with open addressing.  Of the
	structures we had in class, it appeared to have the best Big-O runtimes.
	However, I did not account for the fact that size() would never be called
	and that separate chaining is in factor faster.  This was proven when
	Charlie Ren's hashtable implemented using separate chaining was faster.

	I used a load factor of .5 as that is what we discussed in class as being
	necessary for the Big-O runtimes.

	I created a constructor which required the initial maxCapacity determined
	by the user of the class.  Depending on which dicitonary was used, I
	figured someone would want to specify their own maxCapacity.

	I created a resize method which returned a new hashtable with 2x the
	capacity of the previous hashtable.  The new hashtable included the
	rehashed values from the previous table.

Board class -
	The main portion of our ediiting came during the allWords method.  In this
	method, I updated the used boolean array to ensure that the recursive
	calls would not repeat over the same positions on the board.  I also had
	to check to ensure that if the letter at the current location was a 'Q'
	then a 'Q' and 'U' were added to 'sofar.'  The method ensured that if
	the potential word, represented by 'sofar,' was in fact a word in the
	dictionary that it was added to the 'foundWords' hashtable. 

	For this method, I used recursion at each of the 8 locations surround the
	current location.  Initially, I wanted to remove the letter that the
	deeper recursive call added to sofar and change the board location in
	'used' to false ----> Rookie Mistake.  Jave uses pass by value and as the
	I returned to different stack locations my string was the same as it was
	prior to the recursive call and 'used' just needed to be changed back to
	false after all the recursive calls had been made.

The project was a lot of fun, and I learned a lot from it.  I looked up some other
structures that I heard members of the class talking about, particularly a 'trie'
and found those to be quite interesting.  That structure was particularly appropriate,
but I wanted to stick to a strucutre we learned in class.  You could also have
implemented this project in parallel as each board location can be checked
independently.
