import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class GUI implements ActionListener {

    private JLabel label = new JLabel("Welcome to your very own Notes Board.", SwingConstants.CENTER);
    private JLabel ipAddy = new JLabel("IP Address: ");
    private JLabel port = new JLabel("Port Number: ");
    private JLabel blank = new JLabel("Type your note here: ");
    private JLabel getReq = new JLabel("Enter required properties here: ");
    private JLabel result = new JLabel("THIS NEEDS TO BE LIVE DATA", SwingConstants.CENTER);
    private JFrame frame = new JFrame();
    private JTextField post = null;
    private JTextField gett = null;
    private Client client = null;
    private ArrayList<String> results = null;
    private JComboBox comboBox = null;

    public GUI() {
        this.client = new Client();
        this.results =  new ArrayList<String>();

        // the clickable buttons
        JButton connectButton = new JButton("Connect");
        JButton disconnectButton = new JButton("Disconnect");
        JButton pinButton = new JButton("Pin");
        JButton unpinButton = new JButton("Unpin");
        JButton clearButton = new JButton("Clear");
        JButton shakeButton = new JButton("Shake");
        JButton postButton = new JButton("Post");
        JButton getButton = new JButton("Get");

        //text fields
        JTextField ipAddyy = new JTextField(20);
        ipAddyy.setBounds(100, 20, 165, 25);

        JTextField portNum = new JTextField(20);
        portNum.setBounds(100, 20, 165, 25);

        this.post = new JTextField(20);
        this.post.setBounds(100,20, 165, 25);

        this.gett = new JTextField(20);
        this.gett.setBounds(100, 20, 165, 25);

        this.comboBox = new JComboBox(results.toArray());
        this.comboBox.setModel(new DefaultComboBoxModel(results.toArray()));


        //Button click event listeners
        connectButton.addActionListener(this::connectPerformed);
        disconnectButton.addActionListener(this::disconnectPerformed);
        pinButton.addActionListener(this);
        unpinButton.addActionListener(this);
        clearButton.addActionListener(this::clearPerformed);
        shakeButton.addActionListener(this::shakePerformed);
        postButton.addActionListener(this::postPerformed);
        getButton.addActionListener(e -> {
            try {
                getPerformed(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        comboBox.addActionListener(this);

        // the panel with the button and text
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(comboBox);
        panel.add(label);
        panel.add(ipAddy);
        panel.add(ipAddyy);
        panel.add(port);
        panel.add(portNum);
        panel.add(connectButton);
        panel.add(disconnectButton);
        panel.add(pinButton);
        panel.add(unpinButton);
        panel.add(clearButton);
        panel.add(shakeButton);
        panel.add(blank);
        panel.add(post);
        panel.add(postButton);
        panel.add(getReq);
        panel.add(gett);
        panel.add(getButton);
        panel.add(result);


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
        client.sendRequestMessage("5555 100 100 red blue green");
    }

    public void getPerformed(ActionEvent e) throws IOException {
        if (!gett.getText().isEmpty()) {
            String getReq = "GET " + gett.getText();
            ArrayList<String> content = client.getReturnedNotes(getReq);

            this.comboBox.removeAllItems();
            for (String c : content) {
                this.comboBox.addItem(c);
            }
        }
    }

    public void postPerformed(ActionEvent e) {
        String postReq = "POST " + post.getText();
        client.sendRequestMessage(postReq);
    }

    public void clearPerformed (ActionEvent e) {
        client.sendRequestMessage("CLEAR");
    }

    public void shakePerformed (ActionEvent e) {
        client.sendRequestMessage("SHAKE");
    }

    public void disconnectPerformed (ActionEvent e) {
        client.sendRequestMessage("DISCONNECT");
        client.disconnect();
    }

    // create one Frame
    public static void main(String[] args) {
        new GUI();
    }
}