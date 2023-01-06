/*
 *Enigma class to perform the necessary functions with the three rotors.
 */
public class Enigma
{
    /*
     *Create three rotors.
     */
    private Rotor inner  = new Rotor();
    private Rotor middle = new Rotor();
    private Rotor outer  = new Rotor();

    private int middleCounter = 0, innerCounter = 0;
    private String newRotor = new String();
    /*
     *Constructor for Enigma class.
     */
    public Enigma(String in, String mid, String out)
    {
        inner.setRotor(in);
        middle.setRotor(mid);
        outer.setRotor(out);
    }
    /*
     *Method to encrypt message.
     */
    public String encrypt(String message)
    {
        //Create temporary string to copy the encrypted message.
        String encrypted = new String();
        //Create temp variables to return the index and character of the 
        //part of the string to be encrypted.
        char encryptLetter;
        int index;
        for(int i = 0; i < message.length(); i++){
            encryptLetter = message.charAt(i);
            index = inner.getRotor().indexOf(encryptLetter);
            encryptLetter = outer.getRotor().charAt(index);
            index = middle.getRotor().indexOf(encryptLetter);
            encrypted += outer.getRotor().charAt(index);

            //Once mmessage has been created, rotor the motors as necessary.
            newRotor = inner.rotate();
            inner.setRotor(newRotor);
            innerCounter++;
            if(innerCounter == inner.getRotor().length()){
                newRotor = middle.rotate();
                middle.setRotor(newRotor);
                middleCounter++;
                innerCounter = 0;
                if(middleCounter == middle.getRotor().length()){
                    newRotor = outer.rotate();
                    outer.setRotor(newRotor);
                    middleCounter = 0;
                }
            }
        }
        return encrypted;
    }
    
    /*
     *Method to decrypt message.
     */
    public String decrypt(String message)
    {
        //Create temporary string to copy the decrypted message.
        String decrypted = new String();
        //Create temporary variable to decrypt the message.
        char decryptLetter;
        int index;
        for (int i = 0; i < message.length(); i++){
            decryptLetter = message.charAt(i);
            index = outer.getRotor().indexOf(decryptLetter);
            decryptLetter = middle.getRotor().charAt(index);
            index = outer.getRotor().indexOf(decryptLetter);
            decrypted += inner.getRotor().charAt(index);

            //Rotate the rotors after each decrypted letter.
            newRotor = inner.rotate();
            inner.setRotor(newRotor);
            innerCounter++;
            if(innerCounter == inner.getRotor().length()){
                newRotor =  middle.rotate();
                middle.setRotor(newRotor);
                middleCounter++;
                innerCounter = 0;
                if(middleCounter == middle.getRotor().length()){
                    newRotor = outer.rotate();
                    outer.setRotor(newRotor);
                    middleCounter = 0;
                }
            }
        }
        return decrypted;
    }
}