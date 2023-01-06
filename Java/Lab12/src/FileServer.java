import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Server process for interaction with a client
 */
public class FileServer {
	
	/**
	 * Directory location containing necessary text files
	 */
	private final String filepath = "docs/";
    /**
     * Method that ensures the proper operation of 
     * 
     * @param port Port number to listen on
     */
	private Scanner in;
	private String request = "/continue";
	private Socket client;
	private PrintWriter out;
	
	/**
	 * Sends a list of files within the local "docs/" directory across the established
	 * socket connection
	 */
	public void update() {
		
		File[] files = new File(filepath).listFiles();
		String filenames = "List of filenames: ";
		for (int i = 0; i < files.length; i++){
			if (files[i].isFile()){
				filenames += files[i].getName() + " ";
			}
		}
		out.println(filenames);
	}
	
	/**
	 * Retrieves file content of a file designated by an input
	 * through the socket connection
	 */
	public void request() {
		try {
			out.println("/continue");
			String filename = "docs/" + in.nextLine();
	        File file = new File(filename);
	        if (file.exists()){
	        	Scanner scan = new Scanner(new File(filename));
	        	String fileContent = "";
	        	while (scan.hasNextLine()){
	        		fileContent += scan.nextLine() + "\n";
	        	}
	        	out.println("1" + fileContent + "<ENDTOKEN>");
	        } else {
	        	out.println("0");
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Listens for connection attempts to be made with server
	 * 
	 * @param port Sets port for server to listen on
	 */
	public void listen(int port) {
    	//while (true){
	        try{
	            // (1) Create a ServerSocket to listen on a port
	            ServerSocket svr = new ServerSocket(port);

	            // (2) Wait for a client to connect
	            client = svr.accept();	                        
	
	            // (3) Create a Scanner to read from the socket
	            in = new Scanner(client.getInputStream());
	            out = new PrintWriter(client.getOutputStream(), true);
	            do {
	            	if (in.hasNextLine()){
	            		request = in.nextLine();
	            		if (request.equals("/update")){
	            			this.update();
	            		} else if (request.equals("/request")) {
	            			this.request();
	            		}
	            	}
	            } while (!request.equals("/quit"));
	            
	            svr.close();
	            in.close();
	            client.close();
				
	        } catch (IOException e) {
	            System.out.println("Could not listen on port " + port);
	            System.exit(-1);
	        }
    	//}
    }

    public static void main(String[] args) {
        FileServer us = new FileServer();
        try {
        	us.listen(Integer.parseInt(args[0]));
        } catch (ArrayIndexOutOfBoundsException aioobe){
        	System.err.println("Incorrect number of arguments: 1 needed");
        	System.exit(-1);
    	} catch (NumberFormatException nfe) {
    		System.err.println("Invalid argument: Integer required");
    		System.exit(-1);
        }
    }
}