# TCP Multi-User Chat Application

This repository contains a Java-based TCP multi-user chat application that allows multiple clients to connect to a server and engage in real-time text-based chat. The code is divided into three main components:

## Client

The Client class represents the chat client application.
It provides a graphical user interface (GUI) using Swing for sending and receiving messages.
Users can enter their nickname and send messages to the server and other connected clients.
The client can disconnect gracefully from the server.


## ClientHandler

The ClientHandler class is responsible for handling individual client connections on the server side.
It communicates with the clients, relays messages between clients, and manages client disconnections.
Each connected client is assigned a separate ClientHandler instance.


## Server

The Server class represents the chat server application.
It listens for incoming client connections on a specified port.
When a client connects, a new ClientHandler instance is created to manage that client.
The server can broadcast messages to all connected clients.


## How to Use

To run the chat server, specify the desired port number in the Server class and execute the main method.
Clients can connect to the server by running the Client class, providing the server's hostname or IP address and port number.
Clients enter their nickname and can start chatting with others.


### Dependencies

This project uses Java's Swing library for the client GUI and standard Java sockets for network communication.
