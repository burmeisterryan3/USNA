import java.util.Scanner;
public class Lab04 {
  public static void main(String[] args) {

    // Prompt for the rotors' initial positions
    System.out.println("Please enter the three rotors' definition strings, beginning with their initial positions," +
        " using # in place of a space.  Please do the inner rotor, then the " +
        "middle rotor, then the outer rotor.  (e.g. '#BDFHJLNPRTVXZACEGIKMOQSUWY' ");
    Scanner scan = new Scanner(System.in);

    // Make and fill an array of three Strings to hold the initial rotor settings.
    String[] settings = new String[3];
    for (int i = 0; i < 3; i++) {
      settings[i] = scan.next();
      while (settings[i].length() != 27) {
        System.out.println("Bad length!  Enter again!");
        settings[i] = scan.next();
      }
    }

    // Call the Enigma's constructor to build the machine
    Enigma e = new Enigma(settings[0], settings[1], settings[2]);

    // Ask the user if he is encrypting or decrypting
    String EorD;
    do {
      System.out.print("Are you (E)ncrypting or (D)ecrypting? ");
      EorD = scan.next().toLowerCase();
    } while (EorD.charAt(0) != 'e' && EorD.charAt(0) != 'd');
    boolean ed = (EorD.charAt(0) == 'e');

    // Get the message from the user, and call the appropriate method
    System.out.println("Please enter the message, using # in place of a space:");
    String message = scan.next();
    if (ed)
      System.out.println(e.encrypt(message));
    else
      System.out.println(e.decrypt(message));
  }
}