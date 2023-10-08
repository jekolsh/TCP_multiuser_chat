package TCP_Multiuser_chatt_2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private List<ClientHandler> clients;
    private ExecutorService executor;

    public Server(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
        this.executor = Executors.newFixedThreadPool(10);
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected.");

                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);

                executor.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client == sender) {
                client.sendMessage("You: " + message);
            } else {
                client.sendMessage(message);
            }
        }
    }

    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public static void main(String[] args) {
        int port = 12345; // Change this to the desired port number
        Server server = new Server(port);
        server.start();
    }
}
