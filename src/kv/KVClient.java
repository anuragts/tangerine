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

        // start a new thread that constantly reads the responses from the server
        new Thread(() -> {
            String response;
            try {
                while ((response = reader.readLine()) != null) {
                    System.out.println(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendCommand(String command) {
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
            if (input.equalsIgnoreCase("quit")) {
                client.close();
                break;
            }
            client.sendCommand(input);
        }
    }
}