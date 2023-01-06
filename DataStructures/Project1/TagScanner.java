import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
 
/** A Scanner-like class that reads XML open and close tags.
 */
public class TagScanner {
  /** The regular expression to match a complete tag. */
  private static Pattern tagPat = Pattern.compile("<(/?)(\\w+)\\s*>");
 
  /** The regular expression to match any text that starts with some tag. */
  private static Pattern startTag = Pattern.compile("\\s*</?\\w+\\s*>.*");
 
  /** The underlying Scanner object that will be used to read. */
  private Scanner scan;
 
  /** Creates a new TagScanner from System.in. */
  public TagScanner() {
    this.scan = new Scanner(System.in);
  }
 
  /** Matches the java.util.Scanner constructor. */
  public TagScanner(java.io.File source) throws java.io.FileNotFoundException
    { this.scan = new Scanner(source); }
  /** Matches the java.util.Scanner constructor. */
  public TagScanner(java.io.InputStream source) { this.scan = new Scanner(source); }
  /** Matches the java.util.Scanner constructor. */
  public TagScanner(Readable source) { this.scan = new Scanner(source); }
  /** Matches the java.util.Scanner constructor. */
  public TagScanner(String source) { this.scan = new Scanner(source); }
 
  /** Returns the underlying Scanner instance.
   * You could use this if you\'d like to read something that\'s not
   * a tag from the same source.
   */
  public Scanner getScanner() { return scan; }
 
  /** Returns true if there is another XML tag in the input. */
  public boolean hasNext() {
    return scan.hasNext(startTag);
  }
 
  /** Returns the Tag object for the next XML tag in the input stream.
   * @throws NoSuchElementException if the next token is not an XML tag.
   */
  public Tag next() {
    String fullMatch = scan.findWithinHorizon(tagPat, 0);
    if (fullMatch == null) throw new NoSuchElementException("No tag in scanner");
    Matcher tagMatcher = tagPat.matcher(fullMatch);
    boolean didMatch = tagMatcher.matches();
    assert didMatch;
    boolean open = tagMatcher.group(1).equals("");
    return new Tag(tagMatcher.group(2), open, !open);
  }
}