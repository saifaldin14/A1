import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerConnection extends Thread {
    Socket socket;
    Server server;
    DataInputStream din;
    DataOutputStream dout;
    boolean shouldRun = true;

    public ServerConnection(Socket socket, Server server) {
        super("ServerConnectionThread");
        this.socket = socket;
        this.server = server;
    }

    public void sendStringToClient (String text) {
        try {
            dout.writeUTF(text);
            dout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendStringToAllClients (String text) {
        for (int i = 0; i < server.connections.size(); i++) {
            ServerConnection serverConnection = server.connections.get(i);
            serverConnection.sendStringToClient(text);
        }
    }

    public void run() {
        try {
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());

            while (shouldRun) {

                while(din.available() == 0) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                String textIn = din.readUTF();
                sendStringToAllClients(textIn);
            }

            din.close();
            dout.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
