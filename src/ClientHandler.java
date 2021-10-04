import java.io.*;
import java.net.*;
public class ClientHandler {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void connect(String address, int port) throws IOException {
        clientSocket = new Socket();
        try {
            clientSocket.connect(new InetSocketAddress(address, port), 5000);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new
                    InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new IOException("ERROR: Connection refused please check IP Address and Port and try again");
        }
    }

    public void disconnect() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    private String processRequest(Request request, String ISBN, String TITLE,
                                  String AUTHOR, String PUBLISHER, int YEAR, boolean all) {
        String requestData = request.name() + "\r\n";
        if (all) {
            requestData += "ALL";
        } else {
            requestData += "ISBN " + ISBN + "\r\n" + "TITLE " + TITLE + "\r\n" +
                    "AUTHOR " + AUTHOR + "\r\n" + "PUBLISHER " + PUBLISHER + "\r\n" + "YEAR " + YEAR
                    + "\r\n";
        }
        return requestData;
    }
    public String sendMessage(Request request, String ISBN, String TITLE, String
            AUTHOR, String PUBLISHER, int YEAR,
                              boolean all, boolean bibtex) throws IOException {
        String requestData = processRequest(request, ISBN, TITLE, AUTHOR,
                PUBLISHER, YEAR, all);
        out.println(requestData + "\r\n\\EOF");
        String response = "";
        String line = in.readLine();
        while (line != null && !line.contains("\\EOF")) {
            response = response.concat(line + "\r\n");
            line = in.readLine();
        }
        if (bibtex) {
            String[] splitResponse = response.split("\r\n");
            if (splitResponse.length > 2) {
                response = "";
                for (String s : splitResponse) {
                    String[] splitLine = s.split(" ");
                    if (splitLine[0].contains("ISBN:")) {
                        response = response.concat("@BookEntry{\r\n\tISBN\t= \""
                                + s.substring(splitLine[0].length()).trim() + "\",\r\n");
                    }
                    if (splitLine[0].contains("TITLE:")) {
                        response = response.concat("\tTITLE\t= \"" +
                                s.substring(splitLine[0].length()).trim() + "\",\r\n");
                    }
                    if (splitLine[0].contains("AUTHOR:")) {
                        response = response.concat("\tAUTHOR\t= \"" +
                                s.substring(splitLine[0].length()).trim() + "\",\r\n");
                    }
                    if (splitLine[0].contains("PUBLISHER:")) {
                        response = response.concat("\tPUBLISHER\t= \"" +
                                s.substring(splitLine[0].length()).trim() + "\",\r\n");
                    }
                    if (splitLine[0].contains("YEAR:")) {
                        if (s.substring(splitLine[0].length()).trim().equals("0"))
                            response = response.concat("\tYEAR\t= \"No Value\",\r\n}\r\n");
                        else
                            response = response.concat("\tYEAR\t= \"" + s.substring(splitLine[0].length()).trim() + "\",\r\n}\r\n");
                    }
                }
            }
        }
        return response;
    }
    public boolean isConnected() {
        try {
            out.println("ping");
            return in.readLine().equals("pong");
        } catch (NullPointerException | IOException e) {
            return false;
        }
    } }