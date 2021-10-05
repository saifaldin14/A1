import java.io.*;
import java.net.Socket;
import java.util.*;

public class Request implements Runnable {
    final static String CRLF = "\r\n";
    Socket socket;
    private boolean shouldRun = true;
    private String statusCode = "OK";
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

        while (shouldRun) {
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
                    processPinRequest(splitStr);
                    break;
                case "UNPIN":
                    processUnpinRequest(splitStr);
                    break;
                case "DISCONNECT":
                    processDisconnectRequest();
                    break;
                default:
                    processInvalidRequest();
            }

            //Construct response message (check Web server example)
            String statusLine = null;
            if (statusCode.equals("OK")) {
                statusLine = "OK" + CRLF;
            } else {
                statusLine = "ERROR" + CRLF;
            }
            // Send the status line.
            os.writeBytes(statusLine);

            // Send a blank line to indicate the end of the header lines.
            os.writeBytes(CRLF);
        }

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

        System.out.println(notes.width);
    }

    private void processGetRequest(String[] splitStr) {
        if (splitStr.length < 2) {
            processInvalidRequest();
            return;
        }

        HashMap<Integer, Note> fetchedNotes = new HashMap<Integer, Note>();
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

        // Print message for debugging only
        for (Note note : fetchedNotes.values()) {
            System.out.println(note.getId());
        }

        ArrayList<String> strFetchedNotes = getReturnGet(fetchedNotes);

        try {
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.writeObject(strFetchedNotes);
        } catch (IOException e) {
            e.printStackTrace();
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

    private ArrayList<String> getReturnGet (HashMap<Integer, Note> fetchedNotes) {
        ArrayList<String> getReturn = new ArrayList<String>();

        for (Note note : fetchedNotes.values()) {
            String strNote = "";
            strNote += note.getCoordinates().toString() + " ";
            strNote += note.getNoteDimensions().toString() + " ";
            strNote += note.getColor() + " ";
            strNote += note.getPinStatus() + " ";
            strNote += note.getMessage();
            getReturn.add(strNote);
        }

        return getReturn;
    }
    private void processPostRequest(String[] splitStr) {
        // Requirements: POST x y width height color message
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
        System.out.println(notes.getNote(1).getMessage());
    }

    private void processClearRequest() {
        notes.clear();
        ArrayList<String> strFetchedNotes = new ArrayList<String>();

        try {
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.writeObject(strFetchedNotes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processShakeRequest() {
        notes.shake();
        HashMap<Integer, Note> pinned = notes.getPin();
        ArrayList<String> strFetchedNotes = getReturnGet(pinned);

        try {
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.writeObject(strFetchedNotes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processPinRequest(String[] splitStr) {
        int id = 0;
        try {
            id = Integer.parseInt(splitStr[1]);
        } catch (NumberFormatException e) {
            processInvalidRequest();
            return;
        }

        notes.getNote(id).pinValue();
    }

    private void processUnpinRequest(String[] splitStr) {
        int id = 0;
        try {
            id = Integer.parseInt(splitStr[1]);
        } catch (NumberFormatException e) {
            processInvalidRequest();
            return;
        }

        notes.getNote(id).unpinValue();
    }

    private void processDisconnectRequest() {
        System.out.println("System disconnected");
        shouldRun = false;
    }

    private void processInvalidRequest() {
        statusCode = "ERROR";
    }
}
