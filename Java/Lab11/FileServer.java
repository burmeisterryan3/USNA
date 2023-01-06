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
    public void listen(int port) {
    	while (true){
	        try{
	            // (1) Create a ServerSocket to listen on a port
	            ServerSocket svr = new ServerSocket(port);
	            File[] files = new File(filepath).listFiles();
	            String filenames = "List of filenames: ";
	            for (int i = 0; i < files.length; i++){
	            	if (files[i].isFile()){
	            		filenames += files[i].getName() + " ";
	            	}
	            }
	
	            // (2) Wait for a client to connect
	            Socket client = svr.accept();
	
	            // (3) Create an object to write data to the socket
	            PrintWriter out = new PrintWriter(client.getOutputStream(), true);            
	
	            // (3) Create a Scanner to read from the socket
	            Scanner in = new Scanner(client.getInputStream());
	            
	            out.println(filenames);
	            
	            String filename = "docs/" + in.nextLine();
	            File file = new File(filename);
	            //How to read from disk
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
	        
            	svr.close();
	            in.close();
				
	        } catch (IOException e) {
	            System.out.println("Could not listen on port " + port);
	            System.exit(-1);
	        }
    	}
    }

    public static void main(String[] args) {
        FileServer us = new FileServer();
        try {
        	us.listen(Integer.parseInt(args[0]));
            System.out.println("Server is done.");
        } catch (ArrayIndexOutOfBoundsException aioobe){
        	System.err.println("Incorrect number of arguments: 1 needed");
        	System.exit(-1);
    	} catch (NumberFormatException nfe) {
    		System.err.println("Invalid argument: Integer required");
    		System.exit(-1);
        }
    }
}