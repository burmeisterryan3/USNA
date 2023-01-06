/** Class to test the correctness of a Map implementation.
 * Copyright (C) Dan Roche, 2014.
 * You do NOT have the permission to share this code with others!
 * This is the top secret testing class and only those properly initiated
 * with IC 312 12-week powers may behold its greatness!
 *
 * Note: this is not well documented, because I didn't plan on sharing it.
 * The actual tests that are performed are in the testMap() method.
 *
 * You run the tests with a command line like
 * java MapTest YourClassName
 */

import java.lang.reflect.*;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.concurrent.*;


@SuppressWarnings("unchecked")
public class MapTest implements Callable<String> {
  public static Map<Integer,String> getMap(String mapClassName) throws MTException {
    Class<?> theirMap = null;
    try { theirMap = Class.forName(mapClassName); }
    catch (Exception e) {
      throw new MTException("No " + mapClassName + " class found"); 
    }
    
    try { return (Map<Integer,String>) theirMap.newInstance(); }
    catch (Exception e) {
      throw new MTException("Could not construct a " + mapClassName + " instance.");
    }
  }

  private static void check(int a, int b) throws MTException {
    if (a != b) throw new MTException("ints " + String.valueOf(a) + " and " + String.valueOf(b) + " don't match");
  }

  private static void check(String a, String b) throws MTException {
    if ((a == null || b == null) && (a != null || b != null))
      throw new MTException("strings don't match");
    else if (a == null && b == null) return;
    else if (!a.equals(b)) throw new MTException("strings " + a + " and " + b + " don't match");
  }

  public static void testMap(String mapClassName, char testName) throws MTException {
    Map<Integer,String> m1 = getMap(mapClassName);
    Map<Integer,String> m2 = getMap(mapClassName);
    int[] keys = new int[] {51723, 240679, 78189, 376248, 370794, 597935, 402280, 935976, 598255, 439763, 712786, 23343, 247980, 912692, 216560, 840369, 706331, 265796, 110813, 86838};
    String[] vals = new String[] {"brutedom", "Meleagridae", "desulphurizer", "logomacher", "bathometer", "crickle", "ventoseness", "kipsey", "thymelical", "diffraction", "galactophygous", "Amorua", "Pantalone", "unprovokedly", "geometer", "tauromorphous", "barrigudo", "laryngotracheitis", "farcer", "potestate"};
    boolean fine = true;
    int bigsize = 100000;
    switch(testName) {
      case 'a':
        m1.set(keys[4], vals[4]);
        check(m1.get(keys[4]), vals[4]);
        break;
      case 'b':
        m1.set(keys[5], vals[5]);
        check(m1.size(), 1);
        break;
      case 'c':
        m1.set(keys[9], vals[9]);
        m1.set(keys[8], vals[8]);
        check(m1.get(keys[9]), vals[9]);
        check(m1.get(keys[8]), vals[8]);
        check(m1.get(keys[9]), vals[9]);
        break;
      case 'd':
        m1.set(keys[11], vals[10]);
        m1.set(keys[11], vals[11]);
        check(m1.size(), 1);
        check(m1.get(keys[11]), vals[11]);
        break;
      case 'e':
        for (int i=0; i<20; ++i) {
          m1.set(keys[i], vals[i]);
        }
        for (int i=0; i<10; ++i)
          check(m1.get(keys[i]), vals[i]);
        for (int i=19; i>=5; --i)
          check(m1.get(keys[i]), vals[i]);
        break;
      case 'f':
        for (int i=19; i>=0; --i) {
          m1.set(keys[i], vals[i]);
        }
        check(m1.size(), 20);
        break;
      case 'g':
        m1.set(keys[2],vals[2]);
        m1.set(keys[10],vals[10]);
        check(m1.get(keys[5]), null);
        break;
      case 'h':
        m1.set(keys[3],vals[3]);
        m1.set(keys[8],vals[8]);
        m1.set(keys[3],null);
        check(m1.get(keys[3]), null);
        check(m1.get(keys[8]), vals[8]);
        break;
      case 'i':
        for (int i=0; i<20; ++i) m1.set(keys[i],vals[i]);
        m1.set(keys[6],null);
        m1.set(keys[18],null);
        check(m1.size(), 18);
        break;
      case 'j':
        check(m2.get(keys[7]), null);
        break;
      case 'k':
        check(m2.size(), 0);
        break;
      case 'l':
        for (int i=5; i<10; ++i) m1.set(keys[i],vals[i]);
        m1.set(keys[15], null);
        check(m1.size(), 5);
        check(m1.get(keys[7]), vals[7]);
        check(m1.get(keys[15]), null);
        break;
      case 'm':
        m1.set(keys[17], null);
        check(m1.size(), 0);
        check(m1.get(keys[17]), null);
        for (int i=2; i<7; ++i) m1.set(keys[i],vals[i]);
        m1.set(keys[5], null);
        check(m1.size(), 4);
        check(m1.get(keys[5]), null);
        check(m1.get(keys[6]), vals[6]);
        break;
      case 'n':
        m1.set(keys[6], vals[6]);
        m1.set(keys[7], vals[7]);
        m2.set(keys[8], vals[8]);
        check(m1.get(keys[6]), vals[6]);
        check(m2.get(keys[8]), vals[8]);
        check(m2.size(), 1);
        check(m1.get(keys[8]), null);
        break; 
      case 'o':
        for (int ii=0; ii<bigsize; ii += 2) {
          m1.set(ii, String.valueOf(ii));
        }
        for (int ii=1; ii<bigsize; ii += 2) {
          m1.set(ii, String.valueOf(ii));
        }
        for (int ii=0; ii<bigsize; ++ii) {
          check(m1.get(ii), String.valueOf(ii));
        }
        check(m1.size(), bigsize);
        break;

      default:
        System.err.println("argument to stack must be a-f or p");
        System.exit(5);
    }
  }

  private String mcn;
  private char tn;
  public MapTest(String mapClassName, char testName) {
    mcn = mapClassName;
    tn = testName;
  }
  public String call() {
    try { testMap(mcn, tn); }
    catch (MTException e) {
      return "Incorrect output: " + e.getMessage();
    }
    return null;
  }



  public static void main(String[] args) {
    if (args.length < 1) {
      System.err.println("must specify map class name");
      System.exit(5);
    }
    else if (args.length > 2) {
      System.err.println("too many arguments");
      System.exit(5);
    }
    else if (args.length == 1) {
      // do all possible tests
      String[] testNames = new String[] {
          "a) Insert and get 1 element",
          "b) Insert 1 and check size",
          "c) Insert and get 2 elements",
          "d) Insert 2 and check size",
          "e) Insert and get 20 elements",
          "f) Insert 20 and check size",
          "g) Try to get something not in the map",
          "h) Insert 2 elements and remove 1",
          "i) Insert 20, remove 2, check size",
          "j) Try to get from empty map",
          "k) Check size of empty map",
          "l) Remove something that's not there",
          "m) Insert over a previous removal",
          "n) Modify two lists simultaneously",
          "o) Insert and get 100000 elements",
      };
      ExecutorService runner = Executors.newSingleThreadExecutor();
      int npass = 0;
      for (String tname : testNames) {
        System.out.print(tname);
        System.out.println("... ");
        Future<String> result = runner.submit(new MapTest(args[0], tname.charAt(0)));
        String feedback = null;
        boolean passed = false;
        try {
          feedback = result.get(10, TimeUnit.SECONDS);
          if (feedback == null) passed = true;
        }
        catch (TimeoutException | InterruptedException e) { feedback = "error: out of time"; }
        catch (ExecutionException e) { 
          if (e.getCause() instanceof StackOverflowError) {
            feedback = "error: stack overflow (too many levels of recursion)";
          }
          else {
            StackTraceElement cause = e.getCause().getStackTrace()[0];
            String eclass = e.getCause().toString();
            eclass = eclass.substring(eclass.lastIndexOf('.') + 1);
            if (eclass.length() == 0) eclass = "exception";
            feedback = eclass + " in " + cause.getFileName() + ", line " + String.valueOf(cause.getLineNumber());
          }
        }
        if (passed) {
          System.out.println("   PASSED");
          ++npass;
        }
        else if (feedback == null) {
          System.out.println("   (did not pass)");
        }
        else {
          System.out.println("   " + feedback);
        }
      }
      System.out.println("Passed " + String.valueOf(npass) + " out of "
          + String.valueOf(testNames.length) + " tests");
      if (npass == testNames.length) System.exit(0);
      else System.exit(1);
    }
    else if (args.length == 2) {
      ExecutorService runner = Executors.newSingleThreadExecutor();
      Future<String> result = runner.submit(new MapTest(args[0], args[1].charAt(0)));
      try {
        String res = result.get();
        if (res == null) {
          System.out.println("PASSED");
          System.exit(0);
        }
        else {
          System.out.println(res);
        }
      }
      catch (InterruptedException e) { System.out.println("error: out of time"); }
      catch (ExecutionException e) { 
        if (e.getCause() instanceof StackOverflowError) {
          System.out.println("error: stack overflow (too many levels of recursion)");
        }
        else {
          StackTraceElement cause = e.getCause().getStackTrace()[0];
          System.out.println(e.toString() + " in "
              + cause.getFileName() + ", line "
              + String.valueOf(cause.getLineNumber()));
        }
      }
      System.exit(1);
    }
  }
}

class MTException extends Exception {
  public MTException(String msg) {
    super(msg);
  }
}