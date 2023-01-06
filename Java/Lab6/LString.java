/** A class to keep an all lower-case representation of a String */
public class LString extends MString
{
    /**
     *  Constructor to create char array representation of string
     *  and ensure all chars of array are lower-case.
     *
     *  @param s String input by user to be stored as char array.
     */
    public LString(String s)
    {
        super(s);
        this.toLower();
    }
    
    /**
     *  Used to ensure that all chars of the char array are a lower-case
     *  representation of the letter.
     */
    private void toLower()
    {
        for (int i = 0; i < charArray.length; i++){
            charArray[i] = Character.toLowerCase(charArray[i]);
        }
    }

    /**
     *  Used to set a char at a specific index to a char given by the user.
     *
     *  @param i Index of char array to be replaced.
     *  @param c The char to be at the given index.
     *  @return Returns true if the char can be changed and the index given is
     *          a valid index for the given char array.
     */
    public boolean setCharAt(int i, char c)
    {
        if (super.setCharAt(i, c)){
            if (!Character.isLowerCase(c)){
                this.toLower();
            }
            return true;
        }
        return false;
    }
}
