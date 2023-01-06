import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Client process for interaction between a server <br/>
 * Must establish a connection and relay necessary information
 */
public class FileClient {

    /**
     * @param ip String containing ip address of server
     * @param port Contains port number for connection
     */
    public void connect(String ip, int port) {
        try{
            // (1) Open Socket to server
            Socket client = new Socket(ip, port);

            // (2) Create an object to write data to the socket
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);            

            // (3) Create a Scanner to read from the socket
            Scanner in = new Scanner(client.getInputStream());       

            String files = new String();
            // (5) Read modified data from server
            //why can't i do hasNext or hasNextLine?
            files += in.nextLine();
            
    		System.out.println(files);
    		
    		Scanner scan = new Scanner(System.in);
    		System.out.print("Which file: ");
            files = scan.next();
            
            out.println(files);
            
            String fileContent = in.nextLine();
            if (fileContent.charAt(0) == '0'){
            	System.err.println("File not found!");
            } else if(fileContent.charAt(0) == '1'){
            	fileContent = fileContent.substring(1);
            	PrintWriter fileWrite = new PrintWriter(new File(files));
            	fileWrite.print(fileContent);
            	fileWrite.flush();
            	fileWrite.close();
            }
            client.close();

        } catch (UnknownHostException uhe) {
        	System.err.println("Invalid IP address");
        	System.exit(-1);
        } catch (IOException e) {
            System.out.println("Could not connect on port " + port);
            System.exit(-1);
        } 
    }

    public static void main(String[] args) {
        FileClient uc = new FileClient();
        try {
            uc.connect(args[0], Integer.parseInt(args[1]));
        } catch (ArrayIndexOutOfBoundsException aioobe) {
        	System.err.println("Too few arguments: 2 needed");
        	System.exit(-1);
        } catch (NumberFormatException nfe){
        	System.err.println("Invalid argument for socket: need integer");
        	System.exit(-1);
        }
    }
}