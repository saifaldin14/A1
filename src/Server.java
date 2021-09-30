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
import java.util.* ;

public final class Server {
    ServerSocket serverSocket;
    ArrayList<ServerConnection> connections = new ArrayList<ServerConnection>();
    boolean shouldRun = true;

    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        try {
            serverSocket = new ServerSocket(3333);
            while (shouldRun) {
                Socket socket = serverSocket.accept();
                ServerConnection serverConnection = new ServerConnection(socket, this);
                serverConnection.start();
                connections.add(serverConnection);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


