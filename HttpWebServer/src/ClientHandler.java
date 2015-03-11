

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.StringTokenizer;

class ClientHandler extends Thread {
	  private Socket socket;  					// The accepted socket from the Web server

	  // Start the thread in the constructor
	  public ClientHandler(Socket s) {
	    socket=s;
	    start();
	  }
	  

	  // Read the HTTP request, respond, and close the connection
	  public void run() {
	    try {

	      // Open connections to the socket
	      BufferedReader in=new BufferedReader(new InputStreamReader(
	        socket.getInputStream()));
	      PrintStream out=new PrintStream(new BufferedOutputStream(
	        socket.getOutputStream()));

	      // Read filename from first input line "GET /filename.html or if not in this format, treat as a file not found.
	      String s=in.readLine();
	      System.out.println(s);  // Log the request

	      // Attempt to serve the file.  Catch FileNotFoundException and
	      // return an HTTP error "404 Not Found".  Treat invalid requests the same way.
	      String filename="";
	      StringTokenizer st=new StringTokenizer(s);
	      try {

	        // Parse the filename from the GET command
	        if (st.hasMoreElements() && st.nextToken().equalsIgnoreCase("GET")
	            && st.hasMoreElements())
	          filename=st.nextToken();
	        else
	          throw new FileNotFoundException();  // Bad request

	        // Append trailing "/" with "index.html"
	        if (filename.endsWith("/"))
	          filename+="index.html";

	        // Remove leading / from filename
	        while (filename.indexOf("/")==0)
	          filename=filename.substring(1);

	        // Replace "/" and "\" in path for PC-based servers
	        filename=filename.replace('/', File.separator.charAt(0));

	        // Check for illegal characters to prevent access to super directories
	        if (filename.indexOf("..")>=0 || filename.indexOf(':')>=0
	            || filename.indexOf('|')>=0)
	          throw new FileNotFoundException();

	        // If a directory is requested and the trailing / is missing,
	        // send the client an HTTP request to append it.
	        if (new File(filename).isDirectory()) {
	          filename=filename.replace('\\', '/');
	          out.print("HTTP/1.0 301 Moved Permanently\r\n"+
	            "Location: /"+filename+"/\r\n\r\n");
	          out.close();
	          return;
	        }

	       
	        InputStream ins=new FileInputStream(filename);

	        
	        String mimeType="text/plain";
	        if (filename.endsWith(".html") || filename.endsWith(".htm"))
	          mimeType="text/html";
	        else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg"))
	          mimeType="image/jpeg";
	        else if (filename.endsWith(".gif"))
	          mimeType="image/gif";
	        else if (filename.endsWith(".class"))
	          mimeType="application/octet-stream";
	        out.print("HTTP/1.0 200 OK\r\n"+
	          "Content-type: "+mimeType+"\r\n\r\n");

	       
	        byte[] a=new byte[4096];
	        int n;
	        while ((n=ins.read(a))>0)
	          out.write(a, 0, n);
	        ins.close();
	        out.close();
	       socket.close(); 
	      }
	      catch (FileNotFoundException x) {
	        out.println("HTTP/1.0 404 Not Found\r\n"+
	          "Content-type: text/html\r\n\r\n"+
	          "<html><head></head><body> <h3>404 Not Found <h3> </body></html>\n");
	       
	        out.close();
	        socket.close();
	      }
	    }
	    catch (IOException x) {
	      System.out.println(x);
	    }
	    
	  }
	  
}


