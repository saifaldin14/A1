import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class GUI implements ActionListener {

    private JLabel label = new JLabel("Welcome to your very own Notes Board.", SwingConstants.CENTER);
    private JLabel initializeBoard = new JLabel("Board Initialization (width height colors): ");
    private JLabel getMessage = new JLabel("Display GET results: ");
    private JLabel blank = new JLabel("Type your note here: ");
    private JLabel getReq = new JLabel("Enter required properties here: ");
    private JFrame frame = new JFrame();
    private JTextField post = null;
    private JTextField gett = null;
    private  JTextField initializeBoardText = null;
    private Client client = null;
    private ArrayList<String> results = null;
    private JComboBox comboBox = null;

    public GUI() {
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
        JButton getButton = new JButton("Get");

        //text fields
        this.initializeBoardText = new JTextField(20);
        this.initializeBoardText.setBounds(100, 20, 165, 25);

        this.post = new JTextField(20);
        this.post.setBounds(100,20, 165, 25);

        this.gett = new JTextField(20);
        this.gett.setBounds(100, 20, 165, 25);

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
        comboBox.addActionListener(this);

        // the panel with the button and text
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(label);
        panel.add(initializeBoard);
        panel.add(initializeBoardText);
        panel.add(connectButton);
        panel.add(connectWithoutInitButton);
        panel.add(getMessage);
        panel.add(comboBox);
        panel.add(getReq);
        panel.add(gett);
        panel.add(getButton);
        panel.add(pinButton);
        panel.add(unpinButton);
        panel.add(clearButton);
        panel.add(shakeButton);
        panel.add(blank);
        panel.add(post);
        panel.add(postButton);
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
        this.client = new Client();
        String request = "5555";
        if (!initializeBoardText.getText().isEmpty()) {
            request += " " + initializeBoardText.getText();
            System.out.println(request);
            client.sendRequestMessage(request);
        }
    }

    public void connectWithoutInitPerformed(ActionEvent e) {
        this.client = new Client();
        client.sendRequestMessage("5555");
    }

    public void getPerformed(ActionEvent e) throws IOException {
        if (!gett.getText().isEmpty()) {
            this.comboBox.removeAllItems();

            String getReq = "GET " + gett.getText();
            ArrayList<String> content = client.getReturnedNotes(getReq);

            for (String c : content) {
                this.comboBox.addItem(c);
            }
        }
    }

    public void postPerformed(ActionEvent e) {
        if (!post.getText().isEmpty()) {
            String postReq = "POST " + post.getText();
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
        new GUI();
    }
}