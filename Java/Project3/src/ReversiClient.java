import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class ReversiClient {
	/**
	 * Socket for connection to the server
	 */
	private Socket client;
	private PrintWriter out;
	private Scanner inTerminal, inSocket;
	private final boolean Black = false;
	
	/**
	 * Establishes a connection with a server and continues to provide service
	 * by allowing for communication between the two machines
	 * 
	 * @param ip Contains String representation of IP address of server
	 * @param port Contains the port number to connect on the server
	 */
	protected ReversiClient(String ip, int port) {
		try {
			client = new Socket(ip, port);
			System.out.println("Game started. You are the Black player. It is your turn.");
			out = new PrintWriter(client.getOutputStream(), true);
			inTerminal = new Scanner(System.in);
			inSocket = new Scanner(client.getInputStream());
			String move, newLine, temp;
			boolean whiteTurn, isNotValidMove;
			do {
				String board = "";
				while (inSocket.hasNextLine() && !(temp = inSocket.nextLine()).equals("-1")){
					board += temp + "\n";
				}
				System.out.println(board);
				out.println("finishedRendering");
				temp = inSocket.nextLine();
				if (temp.equals("Able to Move")) {
					out.println("Send me who's turn it is");
				} else if (temp.charAt(0) == 'N'){
					//No legal Spaces... different from game over strings
					//first letter N can identify difference
					System.out.println(temp);
					out.println("Send me who's turn it is");
				} else {
					System.out.println(temp);
					break;
				}
				whiteTurn = inSocket.nextBoolean();
				if (whiteTurn){
					out.println("/received");
					newLine = inSocket.nextLine();
					move = inSocket.nextLine();
					if (!move.equals("/quit")){
						System.out.println("White's move: " + move + ". It is your turn.");
					} else {
						String quitStatement = inSocket.nextLine();
						System.out.println(quitStatement);
						this.close();
					}
				} else {
					move = inTerminal.nextLine();
					if (!move.equals("/quit")) {
						out.println(move);
						isNotValidMove = inSocket.nextBoolean();
						while (!move.equals("/quit") && isNotValidMove){
							out.println("/received");
							newLine = inSocket.nextLine();
							move = inSocket.nextLine();
							System.out.println(move);
							move = inTerminal.nextLine();
							if (!move.equals("/quit")){
								out.println(move);
								isNotValidMove = inSocket.nextBoolean();
							}
						}
					}
					if (move.equals("/quit")){
						out.println(move);
						String quitReceived = inSocket.nextLine();
						System.out.println("You quit. Game is over.");
						out.println("Black has quit. Game is over");
						this.close();
					} else {
						out.println("valid move");
						newLine = inSocket.nextLine();
						move = inSocket.nextLine();
						System.out.println("Black's move: " + move + ". It is White's turn...");
					}
				}
			} while (!move.equals("/quit"));
		} catch (UnknownHostException uhe){
			System.err.println("Could not estabhlish connection to " + ip);
			System.exit(-1);
		} catch (IOException ioe) {
        	System.err.println("Could not connect on port " + port);
        	System.exit(-1);
        }
	}
	
	/**
	 * Closes the socket connection to the server
	 */
	protected void close(){
		try {
			client.close();
			out.close();
			inTerminal.close();
			inSocket.close();
		} catch (IOException e) {
			System.err.println("Client: Error closing socket");
			System.exit(-1);
		}
	}
}
