Burmeister, Ryan
160786

For this homework assignment, I decided to implement an AVL tree.
I used the resource http://pages.cs.wisc.edu/~ealexand/cs367/NOTES/AVL-Trees/index.html to help further my understanding of when to double
rotate and when to rotate a single time.  I assisted MIDN Ren in
his attempt to better understand the the same.

My Node contained additional elements than those we have previously created.  It had to contain the key and string value for the purposes of the map.  It additonally need the left and right subnodes while also the inclusion of a integer which represented the balance of the node.  The balance method within the Node class used the absolute value method to ensure that when calculating the balance of subnodes the negative aspect did not affect the substraction or addition to the height of the current node.

I created four methods to do the rotations and a helper classes for the set and get methods.  The size of the tree was updated whenever a new element was entered or the value of a key already in the tree was set to null.
