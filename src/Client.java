//Create plan for how we will structure the client app
import java.io.* ;
import java.net.* ;
import java.util.*;

public class Client {
    private BufferedReader in = null;
    private PrintWriter pr = null;
    private Socket socket = null;

    public Client() {
        try {
            this.socket = new Socket("localhost", 5555);
            this.pr = new PrintWriter(socket.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRequestMessage(String request) {
        pr.println(request);
        pr.flush();
    }

    public ArrayList<String> getReturnedNotes (String request) {

        sendRequestMessage(request);

        ArrayList<String> ret = new ArrayList<String>();
        try {
            File textData = new File("text.txt");
            Scanner dataReader = new Scanner(textData);
            while (dataReader.hasNextLine()) {
                String data = dataReader.nextLine();
                ret.add(data);
            }
            dataReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println(ret);

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
//    public static void main(String[] args) {
//        BufferedReader in = null;
//        PrintWriter pr = null;
//        try {
//            Socket socket = new Socket("localhost", 5555);
//            pr = new PrintWriter(socket.getOutputStream());
//            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        pr.println("5555 100 100 red blue green");
//        pr.println("POST 1 1 10 10 red hello my name is saif");
//        pr.println("POST 2 1 10 10 blue hello");
//        pr.println("POST 1 2 10 10 green hello my");
//        pr.println("POST 1 3 10 10 red hello my name");
//        pr.println("POST 3 1 10 10 red hello my name is");
//        pr.println("POST 4 1 10 10 blue saif");
//        pr.println("PIN 1");
//        pr.println("PIN 3");
//        pr.println("PIN 5");
//        pr.println("GET PINS");
//        pr.flush();
//    }
}
