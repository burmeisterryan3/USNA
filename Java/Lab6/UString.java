/** A class that keeps an all CAPS representation of a string */
public class UString extends MString
{
    /**
     *  Constructor that receives a string input and ensures that all
     *  chars are capitalized.
     *
     *  @param s String input from user to be stored in all caps.
     */
    public UString (String s)
    {
        super(s);
        this.toUpper();
    }
    
    /**
     *  Used to ensure that all chars of the char array in the string object
     *  are capitalized.
     */
    private void toUpper()
    {
        for (int i = 0; i < charArray.length; i++){
            charArray[i] = Character.toUpperCase(charArray[i]);
        }
    }

    /**
     *  Used to change the char at a specific index of the char array
     *  in the string object.
     *
     *  @param i Index of char array to be changed.]
     *  @param c New char to be at the given index.
     *  @return Returns true if the char can be replaced at a valid index
     *          of the char array.
     */
    public boolean setCharAt(int i, char c)
    {
        if (super.setCharAt(i, c)){
            if (!Character.isUpperCase(c)){
                this.toUpper();
            }
            return true;
        }
        return false;
    }
}