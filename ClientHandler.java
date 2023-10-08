package TCP_Multiuser_chatt_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Server server;
    private PrintWriter out;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            out.println("Enter your nickname: ");
            String username = in.readLine();
            System.out.println("New user joined: " + username);
            server.broadcastMessage(username + " has joined the chat.", this);

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                System.out.println(username + ": " + clientMessage);
                server.broadcastMessage(username + ": " + clientMessage, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void close() {
        try {
            clientSocket.close();
            server.removeClient(this);
            server.broadcastMessage("User has left the chat.", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
