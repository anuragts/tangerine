package kv;

import java.io.*;
import java.net.*;

public class KVClient {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private Thread listenerThread;

    public KVClient(String host, int port) throws IOException {
        // create a socket object and connect to the server.
        this.socket = new Socket(host, port);
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Start a new thread that listens for responses from the server

        // Problem with sockets, they block the thread and you can't execute more
        // commands.
        this.listenerThread = new Thread(() -> {
            try {
                String response;
                while ((response = reader.readLine()) != null) {
                    System.out.println(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.listenerThread.start();
    }

    public void sendCommand(String command) throws IOException {
        writer.println(command);
    }

    public void close() throws IOException {
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 1111;

        KVClient client = new KVClient(host, port);

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while (true) {
            System.out.print("tangerine-cli> ");
            input = consoleReader.readLine();
            System.out.println();
            if ("quit".equalsIgnoreCase(input)) {
                client.close();
                break;
            } else {
                client.sendCommand(input);
            }
        }

    }
}