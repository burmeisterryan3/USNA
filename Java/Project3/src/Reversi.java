import java.io.IOException;
import java.net.UnknownHostException;


public class Reversi {
	
	/**
	 * Contains help message for user to start Reversi game
	 */
	private final String usage = 
			"Usage:\n   Reversi -s <port> \t   Starts the Server for a networked" +
			"game on the requested port\n\t\t\t   e.g. java Reversi -s 12300\n" +
			"   Reversi -c <IP> <port>  Connects as a Client" +
			" to an existing game on the requested socket " +
			"\n\t\t\t   e.g. java Reversi -c 10.10.1.4 12300\n" +
			"   Reversi -h \t\t   prints the usage\n";
	private ReversiServer rs;
	private ReversiClient rc;
	
	/**
	 * Constructor which either prints help message, creates server, 
	 * or creates client for Reversi game</br>Ensures that user
	 * has entered correct command line arguments for the respective
	 * classes attempting to be started
	 * 
	 * @param args Contains the strings necessary to establish
	 * 			   either a ReversiServer or ReversiClietn class
	 */
	public Reversi(String[] args){
		try {
			if (args[0].equals("-h")){
				System.out.println(usage);
			} else if(args[0].equals("-s")){
				rs = new ReversiServer(Integer.parseInt(args[1]));
			} else if(args[0].equals("-c")){
				rc = new ReversiClient(args[1], Integer.parseInt(args[2]));
			} else {
				System.err.println("Invalid argument entered");
				System.exit(-1);
			}
		} catch (ArrayIndexOutOfBoundsException aioobe){
        	System.err.print("Incorrect number of arguments: ");
        	if (args.length == 0){
        		System.err.println("Use -h for help");
        	} else if(args[0].equals("-s")){
        		System.err.println(" need <port>\n Use -h for help");
        		System.exit(-1);
        	} else if(args[0].equals("-c")) {
        		System.err.println(" need <IP> <port>\n Use -h for help");
        		System.exit(-1);
        	}
		} catch (NumberFormatException nfe) {
    		System.err.println("Invalid argument: Integer required for port");
    		System.exit(-1);
        }
	}
	
	public static void main(String[] args) {
		Reversi game = new Reversi(args);
	}

}
