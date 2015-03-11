Abstract:
The web server in java that implements the HTTP/1.1 protocol and able to work with a standard browser. 
Description:
A web Server which is HTTP 1.1 Compliant. The server is able to talk with a browser and send files to it, able to handle most message headers, able to pass few http response codes.
Server Class: It implements that a web server waits for clients to connect, then starts a separate thread to handle the request.
ClientHandler Class: It accepts socket from the Web server. It reads the HTTP request, respond, and close the connection
