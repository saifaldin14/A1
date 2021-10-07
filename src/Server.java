//Create plan for how we will structure the server app
// Create basic template project
// Define Notes Data Structure
// Define GET Method
// Define POST Method
// Define PIN Method
// Define UNPIN Method
// Define CLEAR Method
// Define SHAKE Method
// Define DISCONNECT Method
import java.io.* ;
import java.net.* ;

public final class Server {
    private static boolean shouldRun = true;

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(5555);
            System.out.println("Server listening on port: 5555");
        } catch (IOException e) {
            System.err.println("Could not listen on port: 5555.");
            System.exit(1);
        }

        // Process HTTP service requests in an infinite loop.
        while (shouldRun) {
            // Listen for a TCP connection request.
            Socket connection = serverSocket.accept();

            // Construct an object to process the HTTP request message.
            Request request = new Request(connection);

            shouldRun = request.getShouldRun();
            // Create a new thread to process the request.
            Thread thread = new Thread(request);

            // Start the thread.
            thread.start();
        }
    }
}


