public class MapMain {
  public static void main(String[] args) {
    Map<Integer,String> map = new AlphaMap(); // change this to AlphaMap()
    map.set(222222, "Ernest Hemingway");
    map.set(111111, "Ray Charles");
    map.set(333333, "John F Kennedy");
    System.out.println(map.get(333333)); // should print John F Kennedy
    System.out.println(map.get(222222)); // should print Ernest Hemingway
    String name = map.get(444444);
    if (name == null) 
      System.out.println("444444 is not in the map");   // should do this
    else
      System.out.println("this is not right: " + name); // not this
    System.out.println(map.size());
  }
}