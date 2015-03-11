
import java.io.*;
import java.net.*;

// A Web server waits for clients to connect, then starts a separate thread to handle the request.
public class Server {
  private static ServerSocket serverSocket;

  public static void main(String[] args) throws IOException {
    serverSocket=new ServerSocket(8001);  
    System.out.println("Started");
    for (;;) {
      try {
    	  
        Socket s=serverSocket.accept();  		// Wait for a client to connect
        new ClientHandler(s);  
          
      }
      catch (Exception x) {
    	  
        System.out.println(x);
      }
    }
  }
}