package TCP_Multiuser_chatt_2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private String serverHost;
    private int serverPort;
    private String username;
    private JTextArea chatTextArea;
    private JTextField messageTextField;
    private PrintWriter out;
    private JButton disconnectButton;

    public Client(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void start() {
        JFrame frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());

        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatTextArea);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        messageTextField = new JTextField();
        messageTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageTextField.getText().trim();
                if (!message.isEmpty()) {
                    sendMessageToServer(message);
                    messageTextField.setText("");
                }
            }
        });
        inputPanel.add(messageTextField, BorderLayout.CENTER);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageTextField.getText().trim();
                if (!message.isEmpty()) {
                    sendMessageToServer(message);
                    messageTextField.setText("");
                }
            }
        });
        inputPanel.add(sendButton, BorderLayout.EAST);

        contentPane.add(inputPanel, BorderLayout.SOUTH);

        disconnectButton = new JButton("Disconnect");
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disconnect();
                frame.dispose(); // Close the window when disconnecting
            }
        });
        contentPane.add(disconnectButton, BorderLayout.NORTH);

        frame.setContentPane(contentPane);
        frame.setSize(400, 300);
        frame.setVisible(true);

        String nickname = JOptionPane.showInputDialog(frame, "Enter your nickname:");
        if (nickname != null && !nickname.isEmpty()) {
            username = nickname;

            try {
                Socket socket = new Socket(serverHost, serverPort);
                System.out.println("Connected to the server.");

                out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println(username);

                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                    displayMessage(serverMessage);
                }

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessageToServer(String message) {
        out.println(message);
    }

    public void displayMessage(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chatTextArea.append(message + "\n");
            }
        });
    }

    public void disconnect() {
        out.println("disconnected");
        disconnectButton.setEnabled(false);
        messageTextField.setEnabled(false);
    }

    public static void main(String[] args) {
        String serverHost = "localhost"; // Change this to the server's hostname or IP address
        int serverPort = 12345; // Change this to the server's port number
        Client client = new Client(serverHost, serverPort);
        client.start();
    }
}
