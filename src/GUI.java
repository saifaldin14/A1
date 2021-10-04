import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI implements ActionListener {

    private JLabel label = new JLabel("Welcome to your very own Notes Board.", SwingConstants.CENTER);
    private JLabel ipAddy = new JLabel("IP Address: ");
    private JLabel port = new JLabel("Port Number: ");
    private JLabel blank = new JLabel("Type your note here: ");
    private JLabel getReq = new JLabel("Enter required properties here: ");
    private JLabel result = new JLabel("THIS NEEDS TO BE LIVE DATA", SwingConstants.CENTER);
    private JFrame frame = new JFrame();

    public GUI() {

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

        JTextField post = new JTextField(20);
        post.setBounds(100,20, 165, 25);

        JTextField gett = new JTextField(20);
        gett.setBounds(100, 20, 165, 25);


        //Button click event listeners
        connectButton.addActionListener(this);
        disconnectButton.addActionListener(this);
        pinButton.addActionListener(this);
        unpinButton.addActionListener(this);
        clearButton.addActionListener(this);
        shakeButton.addActionListener(this);
        postButton.addActionListener(this);
        getButton.addActionListener(this);

        // the panel with the button and text
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));
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

    // create one Frame
    public static void main(String[] args) {
        new GUI();
    }
}