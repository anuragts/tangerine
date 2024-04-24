package kv;

import java.io.*;
import java.net.*;

public class KVClient {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public KVClient(String host, int port) throws IOException {
        // create a socket object and connect to the server.
        this.socket = new Socket(host, port);
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendCommand(String command) throws IOException {
        writer.println(command);
        String response = reader.readLine();
        System.out.println("Server response: " + response);
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
            if (input.equalsIgnoreCase("quit")) {
                client.close();
                break;
            }
            client.sendCommand(input);
        }
    }
}