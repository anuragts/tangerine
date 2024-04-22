package kv;

import java.io.*;
import java.net.*;
import java.util.*;

public class KVServer {
    private InMemoryStorage storage;
    private ServerSocket serverSocket;

    public KVServer(InMemoryStorage storage, int port) throws IOException {
        this.storage = storage;
        this.serverSocket = new ServerSocket(port);
        System.out.println("KV Server started on port " + port);
    }

    public void run() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("New connection from " + socket.getInetAddress());
            handleRequest(socket);
        }
    }

    private void handleRequest(Socket socket) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            String input;
            while ((input = reader.readLine()) != null) {
                String[] parts = input.split(" ");
                if (parts[0].equals("put")) {
                    storage.put(parts[1], parts[2]);
                    writer.println("OK");
                } else if (parts[0].equals("get")) {
                    String value = storage.get(parts[1]);
                    if (value != null) {
                        writer.println(value);
                    } else {
                        writer.println("NOT_FOUND");
                    }
                } else if (parts[0].equals("remove")) {
                    storage.remove(parts[1]);
                    writer.println("OK");
                } else if (parts[0].equals("contains")) {
                    boolean contains = storage.containsKey(parts[1]);
                    writer.println(contains ? "TRUE" : "FALSE");
                } else if (parts[0].equals("clear")) {
                    storage.clear();
                    writer.println("OK");
                } else if (parts[0].equals("seeAll")) {
                    writer.println(storage.seeAll());
                } else {
                    writer.println("Unknown command");
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        InMemoryStorage storage = new InMemoryStorage();
        int port = 1111;
        KVServer server = new KVServer(storage, port);
        server.run();
    }
}