/** A class that allows the user to make modifications to a string. */
public class MString extends ICString
{
    /**
     *  Used as a constructor to keep char array of string input by user.
     *
     *  @param s String input from user.
     */
    public MString(String s)
    {
        super(s);
    }

    /**
     *  Replaces a character at a specific index of a char array.
     *
     *  @param i Index of char array to be replaced.
     *  @param c Char to replace at given index.
     *
     *  @return Returns true if the index is within the bounds of the array.
     */
    public boolean setCharAt(int i, char c)
    {
        if (i >= this.length() || i < 0){
            return false;
        }

        charArray[i] = c;
        return true;
    }
    /**
     *  Used to reverse the order of the char array of the string.
     */
    public void reverse()
    {
        char temp;
        for (int i = 0; i < charArray.length/2; i++){
            temp = charArray[charArray.length-1-i];
            charArray[charArray.length-1-i] = charArray[i];
            charArray[i] = temp;
        }
    }
}