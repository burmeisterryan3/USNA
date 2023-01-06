import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


/**
 * Class that accepts connection from ReversiClient and contains
 * the brains of the Reversi game
 */
public class ReversiServer {
	private ServerSocket server;
	private Socket client;
	private PrintWriter out;
	private Scanner inTerminal, inSocket;
	private ReversiBoard board;
	private final boolean White = true;
	/**
	 * Listens for connections to be made on the port "port", and once the connection
	 * has been made, it will allow for continued communication between it and the client <br/>
	 * This Method allows for game play
	 * 
	 * @param port Port number to establish for listening
	 */
	protected ReversiServer(int port) {
		try {
			server = new ServerSocket(port);
			System.out.println("Server starter. Waiting for another player to connect...");
			client = server.accept();
			System.out.println("Game started. You are the White Player. It is Black's turn...");
			out = new PrintWriter(client.getOutputStream(), true);
			inTerminal = new Scanner(System.in);
			inSocket = new Scanner(client.getInputStream());
			String move, response;
			board = new ReversiBoard();
			boolean whiteTurn = false;
			do {
				out.println(board + "-1");
				System.out.println(board);
				//Get response that board was received
				String finishedRendering = inSocket.nextLine();
				if (isAbleToMove(whiteTurn)){
					out.println("Able to Move");
					response = inSocket.nextLine();
				} else {
					if (noMoreSpace()) {
						int numBlack = board.numBlack();
						int numWhite = board.numWhite();
						if (numBlack > numWhite){
							out.println("You win! Final Score: Black " + numBlack + " White " + numWhite);
							System.out.println("Black wins! Final Score: Black " + numBlack + " White " + numWhite);
						} else if (numBlack < numWhite) {
							out.println("White wins! Final Score: Black " + numBlack + " White " + numWhite);
							System.out.println("You win! Final Score: Black " + numBlack + " White " + numWhite);
						} else {
							out.println("Tie game! Score: Black " + numBlack + " White " + numWhite);
							System.out.println("Tie game! Final Score: Black " + numBlack + " White " + numWhite);
						}
						break;
					} else if (whiteTurn){
						out.println("No legal spaces for White to move. It is your turn again!");
						System.out.println("No legal spaces for you to move. It is Black's turn!");
						response = inSocket.nextLine();
						whiteTurn = !whiteTurn;
					} else {
						out.println("No legal spaces for you to move. It is White's turn!");
						System.out.println("No legal spaces for Black to move. It is your turn!");
						response = inSocket.nextLine();
						whiteTurn = !whiteTurn;
					}
				}
				out.println(whiteTurn);
				if (!whiteTurn){
					move = inSocket.nextLine();
					while(!move.equals("/quit")) {
						//checks to see if move is on board
						if (!validInput(move, whiteTurn)) {
							//output that move is invalid
							out.println(true);
							String received = inSocket.nextLine();
							out.println(move + " is not a legal move. The location is outside the board range.");
							move = inSocket.nextLine();
						} else {
							if (isFull(move, whiteTurn)) {
								//output that move is in a location with another piece
								out.println(true);
								String received = inSocket.nextLine();
								out.println(move + " is unavailable. The location is full. Enter new location.");
								move = inSocket.nextLine();
							} else if (!board.makeMove(move, whiteTurn)){
								out.println(true);
								String received = inSocket.nextLine();
								out.println(move + " is not a legal move. The location does not flip opponent's pieces.");
								move = inSocket.nextLine();
							} else {
								//output that move is valid
								move += board.getScore();
								out.println(false);
								inSocket.nextLine();
								out.println(move);
								System.out.println("Black's move: " + move + ". It is your turn.");
								break;
							}
						}
					}
					if (move.equals("/quit")) {
						out.println("quit received");
						String quit = inSocket.nextLine();
						System.out.println(quit);
						this.close();
					}
				} else {
					//Ensure socket received turn
					move = inSocket.nextLine();
					move = inTerminal.nextLine();
					while (!move.equals("/quit")) {
						if (!validInput(move, whiteTurn)) {
							System.out.println(move + " is not a legal move. The location is outside the board range.");
							move = inTerminal.nextLine();
						} else {
							if (isFull(move, whiteTurn)){
								System.out.println(move + " is unavailable. The location is full. Enter new location.");
								move = inTerminal.nextLine();
							} else if (!board.makeMove(move, whiteTurn)){
								System.out.println(move + " is not a legal move. The location does not flip opponent's pieces.");
								move = inTerminal.nextLine();
							} else {
								String score = board.getScore();
								move += score;
								System.out.println("White's move: " + move + ". It is Black's turn...");
								out.println(move);
								break;
							}
						}
					}
					if (move.equals("/quit")){
						out.println(move);
						System.out.println("You quit. Game is over.");
						out.println("White has quit. Game is over");
						this.close();
					}
				}
				whiteTurn = !whiteTurn;
			} while(!move.equals("/quit"));
		} catch (IOException ioe){
        	System.err.println("Could not listen on port " + port);
        	System.exit(-1);
		}
	}
	
	/**
	 * Checks to ensure that the user entered a valid input (e.g. a8 not c9)
	 * 
	 * @param move Contains the String representation of the move desired
	 * @param whiteTurn Contains the boolean value for whose turn it is
	 * @return Returns true if the move is valid
	 */
	private boolean validInput (String move, boolean whiteTurn) {
		boolean input = false;
		move = move.toUpperCase();
		if (move.charAt(0) >= 'A' && move.charAt(0) <= 'H' && move.length() > 1){
			if (move.charAt(1) >= '1' && move.charAt(1) <= '8'){
				input = true;
			}
		}
		return input;
	}
	
	/**
	 * Calls ReversiBoard's method to determine if someone is capable of moving
	 * on there turn
	 * 
	 * @param whiteTurn Tracks whose turn it is
	 * @return Returns true if there is an available move
	 */
	private boolean isAbleToMove(boolean whiteTurn){
		return board.isAbleToMove(whiteTurn);
	}
	
	/**
	 * Calls ReversiBoard's method to determine if there are any spaces on the board
	 * 
	 * @return Returns true if there are no more spaces on the board
	 */
	private boolean noMoreSpace() {
		return board.noMoreSpace();
	}
	
	/**
	 * Calls ReversiBoard's method to determine if there is a piece on a location
	 * attempting to be moved to
	 * 
	 * @param move Contains the String representation of the moved entered
	 * @param whiteTurn Tracks whose turn it is
	 * @return Returns true if the location is full
	 */
	private boolean isFull(String move, boolean whiteTurn) {
		return board.isFull(move);
	}
	
	/**
	 * Ensures that all the output/input streams and sockets are closed
	 */
	protected void close(){
		try {
			client.close();
			server.close();
			out.close();
			inTerminal.close();
			inSocket.close();
		} catch (IOException e) {
			System.err.println("Server: Error closing sockets");
			System.exit(-1);
		}
	}
}
