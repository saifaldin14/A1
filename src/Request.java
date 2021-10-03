import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Request implements Runnable {
    final static String CRLF = "\r\n";
    Socket socket;
    private boolean shouldRun = true;
    private Notes notes = new Notes();

    // Constructor
    public Request(Socket socket) throws Exception {
        this.socket = socket;
    }

    // Implement the run() method of the Runnable interface.
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean getShouldRun() {
        return shouldRun;
    }

    private void processRequest() throws Exception {
        // Get a reference to the socket's input and output streams.
        InputStream is = socket.getInputStream();
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());

        // Set up input stream filters.
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        // Get the request line of the HTTP request message.
        String requestLine = br.readLine();

        String[] splitStr = requestLine.split("\\s+");

        String methodType = splitStr[0];

        switch (methodType) {
            case "5555":
                processInitializeRequest(splitStr);
                break;
            case "GET":
                processGetRequest(splitStr);
                break;
            case "POST":
                processPostRequest(splitStr);
                break;
            case "CLEAR":
                processClearRequest();
                break;
            case "SHAKE":
                processShakeRequest();
                break;
            case "PIN":
                processPinRequest();
                break;
            case "UNPIN":
                processUnpinRequest();
                break;
            default:
                processInvalidRequest();
        }
        //Construct response message (check Web server example)

        // Close streams and socket.
        os.close();
        br.close();
        socket.close();
    }

    private void processInitializeRequest(String[] splitStr) {
        // Initializing notes needs at least 4 elements (<port number>, <width>, <height>, <color>)
        if (splitStr.length < 4) {
            processInvalidRequest();
            return;
        }

        int width = 0;
        int height = 0;
        ArrayList<String> colors = new ArrayList<String>();

        try {
            width = Integer.parseInt(splitStr[1]);
            height = Integer.parseInt(splitStr[2]);
        } catch (NumberFormatException e) {
            processInvalidRequest();
            return;
        }

        for (int i = 3; i < splitStr.length; i++) {
            colors.add(splitStr[i]);
        }

        notes = new Notes(width, height, colors);
    }

    private void processGetRequest(String[] splitStr) {
        if (splitStr.length < 2) {
            processInvalidRequest();
            return;
        }

        ArrayList<Note> fetchedNotes = new ArrayList<Note>();
        String color = "";
        ArrayList<Integer> coordinates = new ArrayList<Integer>();
        String refersTo = "";

        if (splitStr[1].equals("PINS")) {
            fetchedNotes = notes.getPin();
        } else {
            if (splitStr[1].contains("color")) {
                String[] colorSplit = splitStr[1].split("=");
                color = colorSplit[1];

                if (splitStr.length > 2) {
                    coordinates = processGetContains(splitStr);
                    refersTo = processGetRefersTo(splitStr);
                }
            } else {
                coordinates = processGetContains(splitStr);
                refersTo = processGetRefersTo(splitStr);
            }

            fetchedNotes = notes.getRegular(color, coordinates, refersTo);
        }
    }

    private ArrayList<Integer> processGetContains(String[] splitStr) {
        ArrayList<Integer> coordinates = new ArrayList<Integer>();
        int i = 0;

        while (i < splitStr.length && !splitStr[i].contains("contains"))
            i++;

        try {
            coordinates.add(Integer.parseInt(splitStr[i + 1]));
            coordinates.add(Integer.parseInt(splitStr[i + 2]));
        } catch (ArrayIndexOutOfBoundsException e) {
            processInvalidRequest();
            return coordinates;
        } catch (NumberFormatException e) {
            processInvalidRequest();
            return coordinates;
        }
        return coordinates;
    }

    private String processGetRefersTo (String[] splitStr) {
        String refersTo = "";
        int i = 0;

        while (i < splitStr.length && !splitStr[i].contains("refersTo"))
            i++;

        try {
            refersTo += splitStr[i + 1];

            for (int j = i + 2; i < splitStr.length; i++) {
                refersTo += " " + splitStr[i];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            processInvalidRequest();
            return refersTo;
        }

        return refersTo;
    }

    private void processPostRequest(String[] splitStr) {
        // Requirements: POST x y width height message
        if (splitStr.length < 7) {
            processInvalidRequest();
            return;
        }

        try {
            int x = Integer.parseInt(splitStr[1]);
            int y = Integer.parseInt(splitStr[2]);
            int width = Integer.parseInt(splitStr[3]);
            int height = Integer.parseInt(splitStr[4]);
            String color = splitStr[5];

            String message = "";

            for (int i = 6; i < splitStr.length; i++) {
                message += splitStr[i] + " ";
            }

            notes.post(x, y, width, height, color, message);
        } catch (NumberFormatException e) {
            processInvalidRequest();
            return;
        }
    }

    private void processClearRequest() {
        notes.clear();
    }

    private void processShakeRequest() {
        notes.shake();
    }

    private void processPinRequest() {
    }

    private void processUnpinRequest() {

    }

    private void processInvalidRequest() {

    }

    private static void sendBytes(FileInputStream fis,
                                  OutputStream os) throws Exception {
        // Construct a 1K buffer to hold bytes on their way to the socket.
        byte[] buffer = new byte[1024];
        int bytes = 0;

        // Copy requested file into the socket's output stream.
        while ((bytes = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
        }
    }

    private static String contentType(String fileName) {
        if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        }
        if(fileName.endsWith(".ram") || fileName.endsWith(".ra")) {
            return "audio/x-pn-realaudio";
        }
        return "application/octet-stream" ;
    }
}
