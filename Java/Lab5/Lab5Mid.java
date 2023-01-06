import java.util.Scanner;

/** A class to store information about a Midshipman */
public class Lab5Mid {

  /** The alpha of this midshipman. Each Midshipman has an alpha code, a
   *  six-digit number unique to that midshipman. */
  public int alpha;
  /** The first name of this midshipman. Many midshipmen may have the same
   *  first name. */
  public String firstName;
  /** The last name of this midshipman. Midshipmen may share last names. */
  private String lastName;
  /** The company of this midshipman.  A company is a group of Midshipmen
   *  under a single commanding officer. */
  public int company;

  /** 
   * Used to construct a Midshipman if alpha, first name, last name, and
   * company are known.
   *
   * @param alf The alpha of the midshipman.
   * @param fn The first name of the midshipman.
   * @param ln The last name of the midshipman.
   * @param co The company of the midshipman.
   */
  public Lab5Mid(int alf, String fn, String ln, int co) {
    alpha = alf;
    firstName = fn;
    lastName = ln;
    company = co;
  }

  /** 
   * Used to construct a Midshipman if alpha, etc are not known.  This
   * constructor will request information from the user, listing to a response
   * on the provided Scanner.
   *
   * @param in A Scanner to use to retrieve user responses to output queries.
   */
  public Lab5Mid(Scanner in) {
    System.out.println("Enter alpha, first name, last name, company:");
    alpha = in.nextInt();
    firstName = in.next();
    lastName = in.next();
    company = in.nextInt();
  }

  /** 
   * Returns true if the Midshipman is in the stated company.
   *
   * @param c Candidate company to test upon.
   * @return true if the midshipman is in the input company.
   */
  public boolean isInCompany(int c){
    return company == c;
  }

  /** Returns a String representation of this Midshipman. */
  @Override
  public String toString() {
    return "MIDN " + lastName + ", " + firstName + " (" + alpha + ") Co " +
      company;
  }
}
