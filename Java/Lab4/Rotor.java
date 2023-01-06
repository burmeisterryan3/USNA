/*
 *This class will contain the three strings for the enigma.
 */
public class Rotor
{
    private String rotor = new String();
    /*
     *Set inner rotor.
     */
    public void setRotor(String rotor)
    {
        this.rotor = rotor;
    }
    public String getRotor()
    {
        return rotor;
    }

    /*
     *Rotate inner rotor.
     */
    public String rotate()
    {
        String rotatedRotor = new String();
        rotatedRotor += rotor.charAt(rotor.length()-1);
        for(int i = 0; i < rotor.length()-1; i++){
            rotatedRotor += rotor.charAt(i);
        }
        return rotatedRotor;
    } 
}