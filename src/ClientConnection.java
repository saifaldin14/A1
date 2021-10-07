import java.io.* ;
import java.net.* ;
import java.util.*;

public class ClientConnection {
    private BufferedReader in;
    private PrintWriter pr;
    private Socket socket;
    private BufferedReader br;

    public ClientConnection() {
        try {
            this.socket = new Socket("localhost", 5555);
            this.pr = new PrintWriter(socket.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRequestMessage(String request) {
        pr.println(request);
        pr.flush();
    }

    public synchronized ArrayList<String> getReturnedNotes(String request) throws IOException {

        sendRequestMessage(request);
        ArrayList<String> ret = new ArrayList<String>();

        String str = "";
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        str = br.readLine();
        while (str.equals("OK") || str.equals("ERROR") || str.equals("")) {
            if (str.equals("Nothing found"))
                break;
            str = br.readLine();
        }

        String[] temp = str.split(":");
        for (String s : temp) {
            ret.add(s);
        }

        return ret;
    }

    public void disconnect() {
        try {
            in.close();
            pr.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
