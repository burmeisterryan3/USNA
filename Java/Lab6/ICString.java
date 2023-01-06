/** An immutable String class */
public class ICString {
    /** The char array representation of string */
    protected char[] charArray;

    /**
     *  Used to construct the char array given the string
     *  input by the user.
     *
     *  @param s The string input by the user.
     */
    public ICString(String s) {
        charArray = s.toCharArray();
    }

    /**
     *  Used to return the length of the string.
     *
     *  @return  Returns an int of the length of the string.
     */
    public int length() {
        return charArray.length;
    }

    /**
     *  Used to convert the char array to a string.
     *
     *  @return Returns the string representation of the char array.
     */
    public String toString() {
        return new String(charArray);
    }
}