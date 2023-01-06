/** An XML tag class. Tags have a name and are either open, close,
 * or both (meaning self-closing).
 * So <head> would have name "head" and is an open tag only,
 * </p> would have name "p" and is a close tag only, and
 * <br /> has name "br" and is open and close (i.e., self-closing).
 */
public class Tag {
  private String name;
  private boolean open;
  private boolean close;
 
  public Tag(String name, boolean open, boolean close) {
    this.name = name;
    this.open = open;
    this.close = close;
  }
 
  public String getName() { return this.name; }
  public boolean isOpen() { return this.open; }
  public boolean isClose() { return this.close; }
 
  public String toString() {
    return "<" + (isClose() ? "/" : "") + getName() + ">";
  }
}