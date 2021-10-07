import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class Client implements ActionListener {

    private JLabel label = new JLabel("Welcome to your very own Notes Board.", SwingConstants.CENTER);
    private JLabel initializeBoard = new JLabel("Board Initialization (width height colors): ");
    private JLabel getMessage = new JLabel("Display GET results: ");
    private JLabel blank = new JLabel("Type your note here: ");
    private JLabel getReq = new JLabel("Enter required properties here: ");
    private JFrame frame = new JFrame();
    private JTextField post = null;
    private JTextField gett = null;
    private  JTextField initializeBoardText = null;

    //FOR GET METHOD
    private JLabel color = new JLabel("Color:");
    private JLabel data = new JLabel("Data:");
    private JLabel refersTo  = new JLabel("Refers To:");
    private  JTextField getColor = null;
    private JTextField getData = null;
    private JTextField getRefer = null;

    //FOR POST METHOD
    private JLabel x = new JLabel("X:");
    private JLabel y = new JLabel("Y:");
    private JLabel width = new JLabel("Width:");
    private JLabel height = new JLabel("Height:");
    private JLabel col = new JLabel("Color:");
    private JLabel message = new JLabel("Message:");
    private JTextField postX = null;
    private JTextField postY = null;
    private JTextField postWidth = null;
    private JTextField postHeight = null;
    private JTextField postColor = null;
    private JTextField postMessage = null;

    //FOR BOARD INITIALIZATION
    private JLabel wid = new JLabel("Width:");
    private JLabel hei = new JLabel("Height:");
    private JLabel colo = new JLabel("Colors:");
    private JTextField boardWidth = null;
    private JTextField boardHeight = null;
    private JTextField boardColors = null;

    private ClientConnection client = null;
    private ArrayList<String> results = null;
    private JComboBox comboBox = null;

    public Client() {
        this.results =  new ArrayList<String>();

        // the clickable buttons
        JButton connectButton = new JButton("Connect");
        JButton connectWithoutInitButton = new JButton("Connect without Init");
        JButton disconnectButton = new JButton("Disconnect");
        JButton pinButton = new JButton("Pin");
        JButton unpinButton = new JButton("Unpin");
        JButton clearButton = new JButton("Clear");
        JButton shakeButton = new JButton("Shake");
        JButton postButton = new JButton("Post");
        JButton getButton = new JButton("Regular Get");
        JButton getPinButton = new JButton("PIN Get");
        //text fields
        this.initializeBoardText = new JTextField(20);
        this.initializeBoardText.setBounds(100, 20, 165, 25);

        this.post = new JTextField(20);
        this.post.setBounds(100,20, 165, 25);

        this.gett = new JTextField(20);
        this.gett.setBounds(100, 20, 165, 25);

        //NEW CODE FOR GET METHOD
        this.getColor = new JTextField(20);
        this.getColor.setBounds(10,20,16,25);

        this.getData = new JTextField(20);
        this.getData.setBounds(10,20,16,25);

        this.getRefer = new JTextField(20);
        this.getRefer.setBounds(10,20,16,25);

        //NEW CODE FOR POST METHOD
        this.postX = new JTextField(20);
        x.setBounds(10, 20, 16, 15);

        this.postY = new JTextField(20);
        y.setBounds(10, 20, 16, 15);

        this.postWidth = new JTextField(20);
        this.postWidth.setBounds(10, 20, 16, 25);

        this.postHeight = new JTextField(20);
        this.postHeight.setBounds(10,20,16,15);

        this.postColor = new JTextField(20);
        this.postColor.setBounds(10,20,16,15);

        this.postMessage = new JTextField(20);
        this.postMessage.setBounds(10,20,16,15);

        //NEW CODE FOR BOARD INITIALIZATION
        this.boardWidth = new JTextField(20);
        this.boardWidth.setBounds(10,20,16,15);

        this.boardHeight = new JTextField(20);
        this.boardHeight.setBounds(10,20,16,15);

        this.boardColors = new JTextField(20);
        this.boardColors.setBounds(10,20,16,15);

        this.comboBox = new JComboBox(results.toArray());
        this.comboBox.setModel(new DefaultComboBoxModel(results.toArray()));


        //Button click event listeners
        connectButton.addActionListener(this::connectPerformed);
        connectWithoutInitButton.addActionListener(this::connectWithoutInitPerformed);
        disconnectButton.addActionListener(this::disconnectPerformed);
        pinButton.addActionListener(this::pinPerformed);
        unpinButton.addActionListener(this::unpinPerformed);
        clearButton.addActionListener(this::clearPerformed);
        shakeButton.addActionListener(e1 -> {
            try {
                shakePerformed(e1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        postButton.addActionListener(this::postPerformed);
        getButton.addActionListener(e -> {
            try {
                getPerformed(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        getPinButton.addActionListener(e -> {
            try {
                getPinPerformed(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        comboBox.addActionListener(this);

        // the panel with the button and text
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(label);
        panel.add(initializeBoard);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(wid);
        panel.add(boardWidth);
        panel.add(hei);
        panel.add(boardHeight);
        panel.add(colo);
        panel.add(boardColors);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));

        panel.add(connectButton);
        panel.add(connectWithoutInitButton);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(getMessage);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(comboBox);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(color);
        panel.add(getColor);
        panel.add(data);
        panel.add(getData);
        panel.add(refersTo);
        panel.add(getRefer);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(getButton);
        panel.add(getPinButton);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(pinButton);
        panel.add(unpinButton);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(clearButton);
        panel.add(shakeButton);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(blank);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));

        //FOR POST METHOD
        panel.add(x);
        panel.add(postX);
        panel.add(y);
        panel.add(postY);
        panel.add(width);
        panel.add(postWidth);
        panel.add(height);
        panel.add(postHeight);
        panel.add(col);
        panel.add(postColor);
        panel.add(message);
        panel.add(postMessage);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(postButton);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(disconnectButton);

        // set up the frame and display it
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Notes Application");
        frame.pack();
        frame.setVisible(true);
    }

    // process the button clicks
    public void actionPerformed(ActionEvent e) {

    }

    public void connectPerformed(ActionEvent e) {
        this.client = new ClientConnection();
        String request = "5555";
        String fields = boardWidth.getText() + " " + boardHeight.getText() + " " + boardColors.getText();

        if (!fields.isEmpty()) {
            request += " " + fields;
            client.sendRequestMessage(request);
        }
    }

    public void connectWithoutInitPerformed(ActionEvent e) {
        this.client = new ClientConnection();
        client.sendRequestMessage("5555");
    }

    public void getPerformed(ActionEvent e) throws IOException {
        String fields = "";

        if (!getColor.getText().isEmpty()) {
            fields += "color=" + getColor.getText() + " ";
        }

        if (!getData.getText().isEmpty()) {
            fields += "data= " + getData.getText() + " ";
        }

        if (!getRefer.getText().isEmpty()) {
            fields += "refersTo=" + getRefer.getText();
        }
        if (!fields.isEmpty()) {
            this.comboBox.removeAllItems();

            String getReq = "GET " + fields;
            ArrayList<String> content = client.getReturnedNotes(getReq);

            for (String c : content) {
                this.comboBox.addItem(c);
            }
        }
    }

    public void getPinPerformed(ActionEvent e) throws IOException {
        this.comboBox.removeAllItems();

        String getReq = "GET PINS";
        ArrayList<String> content = client.getReturnedNotes(getReq);

        for (String c : content) {
            this.comboBox.addItem(c);
        }
    }

    public void postPerformed(ActionEvent e) {
        String fields = "";

        fields += postX.getText() + " " + postY.getText() + " " + postWidth.getText() + " " + postHeight.getText();
        fields += " " + postColor.getText() + " " + postMessage.getText();

        if (!fields.isEmpty()) {
            String postReq = "POST " + fields;
            client.sendRequestMessage(postReq);
        }
    }

    public void pinPerformed(ActionEvent e) {
        String x = String.valueOf(comboBox.getSelectedItem());
        String[] splitStr = x.split("\\s+");
        String request = "PIN " + splitStr[0];
        client.sendRequestMessage(request);
    }

    public void unpinPerformed(ActionEvent e) {
        String x = String.valueOf(comboBox.getSelectedItem());
        String[] splitStr = x.split("\\s+");
        String request = "UNPIN " + splitStr[0];
        client.sendRequestMessage(request);
    }

    public void clearPerformed (ActionEvent e) {
        this.comboBox.removeAllItems();
        client.sendRequestMessage("CLEAR");
    }

    public void shakePerformed (ActionEvent e) throws IOException {
        this.comboBox.removeAllItems();
        ArrayList<String> content = client.getReturnedNotes("SHAKE");

        for (String c : content) {
            this.comboBox.addItem(c);
        }
    }

    public void disconnectPerformed (ActionEvent e) {
        client.sendRequestMessage("DISCONNECT");
        client.disconnect();
    }

    // create one Frame
    public static void main(String[] args) {
        new Client();
    }
}