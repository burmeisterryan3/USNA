import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Client process for interaction between a server <br/>
 * Must establish a connection and relay necessary information
 */
public class FileClient {
	
	private Socket client;
	private PrintWriter out;
	private Scanner in;
	
	/**
	 * Method for the purpose of retrieving a file from the server and saving the file locally with
	 * the same name
	 * 
	 * @param file Filename to retrieve from server
	 * @return returns a message as to whether or not the file was retrieved and written successfully
	 */
	public String getFile(String file) {
		String returnStatement = null;
		try {
			out.println("/request");
			returnStatement = in.nextLine();
	        out.println(file);
	        
	        String fileContent = in.nextLine();
	        if (fileContent.charAt(0) == '0'){
	        	returnStatement = "File not found!";
	        } else if(fileContent.charAt(0) == '1'){
	        	fileContent = fileContent.substring(1);
	        	PrintWriter fileWrite;
	        	fileWrite = new PrintWriter(new File(file));
	        	fileWrite.flush();
	    		fileWrite.close();
        		returnStatement = "File saved!";
	        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        } catch (IOException ioe){
        	ioe.printStackTrace();
        } finally {
            return returnStatement;
        }
	}
	
	/**
	 * Allows the user to quit in an appropriate manner <br/>
	 * --Implementation is left to FileClient object
	 */
	public void quit() {
		try {
			out.println("/quit");
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Used to update the user as to the files accessible within the local "docs/" directory
	 * 
	 * @return Returns a list of files within the local "docs/" directory
	 */
	public String update(){
		String files = new String();
		try {
			// (2) Create an object to write data to the socket
	        out = new PrintWriter(client.getOutputStream(), true);            
	
	        // (3) Create a Scanner to read from the socket
	        in = new Scanner(client.getInputStream());      
	        
	        out.println("/update");
	        // (5) Read modified data from server
	        files += in.nextLine();
	        
		} catch (IOException ioe){
			ioe.printStackTrace();
		} finally {
	        return files;
		}
	}

    /**
     * Establishes connection between FileClient and FileServer objects
     * 
     * @param ip String containing ip address of server
     * @param port Contains port number for connection
     */
    public void connect(String ip, int port) {
        try{
            // (1) Open Socket to server
            client = new Socket(ip, port);
        } catch (UnknownHostException uhe) {
        	System.err.println("Invalid IP address");
        	System.exit(-1);
        } catch (IOException e) {
            System.out.println("Could not connect on port " + port);
            System.exit(-1);
        } 
    }

    public static void main(String[] args) {
        FileClient fc = new FileClient();
        try {
            MyGUI gui= new MyGUI(fc, args[0], Integer.parseInt(args[1]));
        } catch (ArrayIndexOutOfBoundsException aioobe) {
        	System.err.println("Too few arguments: 2 needed");
        	System.exit(-1);
        } catch (NumberFormatException nfe){
        	System.err.println("Invalid argument for socket: need integer");
        	System.exit(-1);
        }
    }
}
