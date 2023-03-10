/* SI 335 Spring 2015
 * Project 3
 * YOUR NAME HERE
 */

import java.util.*;
import java.io.*;

public class solvemaze {
  
  /* This function takes in a 2D-array that stores a maze description,
     and returns a list of "moves" to make in order to solve the maze.
   */
  static List<Character> calcMoves(char[][] maze) {
    // THIS IS THE FUNCTION YOU NEED TO RE-WRITE!
    // Width and height calculated from maze size, minus borders
    int w = maze.length - 2;
    int h = maze[0].length - 2;

    // Start (curY, curX) at the starting position
    int curY = 1;
    int curX = 0;

    // The solution here is terrible: it just randomly mills about until
    // it happens upon the end or it runs out of points.
    ArrayList<Character> moves = new ArrayList<Character>();
    Random rgen = new Random();
    while (moves.size() < 4*h*w) {
      int nextY = curY;
      int nextX = curX;
      char nextMove;
      // randomly choose a direction
      if (rgen.nextBoolean()) {
        // go left/right
        if (rgen.nextBoolean()) {
          nextX -= 1;
          nextMove = 'L';
        }
        else {
          nextX += 1;
          nextMove = 'R';
        }
      }
      else {
        // go up/down
        if (rgen.nextBoolean()) {
          nextY -= 1;
          nextMove = 'U';
        }
        else {
          nextY += 1;
          nextMove = 'D';
        }
      }
      if (nextX < 0 || nextX > w+1 || maze[nextY][nextX] == 'X') {
        // illegal move; give up and try another.
        continue;
      }
      moves.add(nextMove);
      curY = nextY;
      curX = nextX;
    }

    return moves;
  }

  /* Checks whether a row of text contains only valid maze characters */
  static boolean validRow(String row) {
    // YOU DON'T NEED TO CHANGE THIS FUNCTION.
    for (int i=0; i < row.length(); ++i) {
      if (" OX".indexOf(row.charAt(i)) < 0) return false;
    }
    return true;
  }

  /* Reads in a maze into a double array of chars. */
  static char[][] readMaze(InputStream in) throws IOException {
    // YOU DON'T NEED TO CHANGE THIS FUNCTION.
    BufferedReader bin = new BufferedReader(new InputStreamReader(in));
    int width = 0;
    String line;
    ArrayList<String> rows = new ArrayList<String>();
    while ((line = bin.readLine()) != null) {
      if (! validRow(line)) {
        throw new IOException("Invalid maze characters in row: " + line);
      }
      // dirty rtrim
      line = ("!" + line).trim().substring(1);
      rows.add(line);
      if (line.length() > width) width = line.length();
    }
    int height = rows.size();
    assert height >= 2;
    assert width >= 2;
    char[][] maze = new char[height][width];
    for (int i=0; i<height; ++i) {
      Arrays.fill(maze[i], ' ');
      String row = rows.get(i);
      row.getChars(0, row.length(), maze[i], 0);
    }
    return maze;
  }

  /* Writes the moves in the list to standard out, one per line. */
  static void writeMoves(List<Character> moves) {
    for (char m : moves) {
      System.out.println(m);
    }
  }

  public static void main(String[] args) {
    // THIS IS THE MAIN METHOD. YOU DON'T NEED TO CHANGE IT.
    char[][] maze = null;
    try{ maze = readMaze(System.in); }
    catch(IOException e) { e.printStackTrace(); System.exit(1); }
    List<Character> moves = calcMoves(maze);
    writeMoves(moves);
  }
}
