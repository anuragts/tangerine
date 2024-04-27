package kv;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import utils.*;

public class KVServer {
    private InMemoryStorage storage;
    private ServerSocket serverSocket;
    private ExecutorService executor;
    private File snapshotFile;

    public KVServer(InMemoryStorage storage, int port) throws IOException {
        this.storage = storage;
        this.serverSocket = new ServerSocket(port);
        this.executor = Executors.newFixedThreadPool(10); // fixed no of threads (can be increased or decreased
                                                          // according to need)
        System.out.println("KV Server started on port " + port);
        snapshotFile = new File("snapshots");
        snapshotFile.mkdirs();

        // Initialize GlobSnapShot and load the existing data
        GlobSnapShot globSnapShot = new GlobSnapShot();
        Map<String, String> existingData = globSnapShot.readSnapshot();
        for (Map.Entry<String, String> entry : existingData.entrySet()) {
            storage.set(entry.getKey(), entry.getValue());
        }
    }

    public void run() throws IOException {
        while (true) {
            // accept a connection from the client and handle the request in a separate
            // thread using handleRequest method.
            Socket socket = serverSocket.accept();
            executor.submit(() -> {
                try {
                    handleRequest(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            // check snapshot files and read the files and set or sync the current in memory
            // storage to the snapshot data
            File[] snapshotFiles = snapshotFile.listFiles();
            if (snapshotFiles != null) {
                for (File file : snapshotFiles) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            // snapshot is '=' kv pair because that is how hashmap in java stores data
                            String[] parts = line.split("=");
                            if (parts.length >= 2) {
                                storage.set(parts[0], parts[1]);
                            } else {
                                System.out.println("Invalid line in snapshot file: " + line);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void handleRequest(Socket socket) throws IOException {
        // create a new GlobSnapShot object to save the snapshot.
        GlobSnapShot globSnapShot = new GlobSnapShot();
        // create a new JSONParser object to parse string to object. This is essential
        // because
        // getAll returns a string of object.

        // get input stream and output stream from the socket and create a buffered
        // reader and a writer
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            String input;
            while ((input = reader.readLine()) != null) {
                String[] parts = input.split(" ");
                switch (parts[0]) {
                    case "set":
                        if (parts.length == 4) {
                            int ttl = Integer.parseInt(parts[3]);
                            storage.set(parts[1], parts[2], ttl);
                            System.out.println("TTL set to " + ttl);
                        } else {
                            storage.set(parts[1], parts[2]);
                        }

                        globSnapShot.saveToGlob(JSONParser.parseJSON(storage.seeAll()));
                        writer.println("OK");
                        break;
                    case "get":
                        String value = storage.get(parts[1]);
                        if (value != null) {
                            writer.println(value);
                        } else {
                            writer.println("NOT_FOUND");
                        }
                        break;
                    case "remove":
                        storage.remove(parts[1]);
                        globSnapShot.saveToGlob(JSONParser.parseJSON(storage.seeAll()));
                        writer.println("OK");
                        break;
                    case "contains":
                        boolean contains = storage.containsKey(parts[1]);
                        writer.println(contains ? "TRUE" : "FALSE");
                        break;
                    case "clear":
                        storage.clear();
                        writer.println("OK");
                        break;
                    case "seeAll":
                        writer.println(storage.seeAll());
                        break;
                    default:
                        writer.println("Unknown command");
                        break;
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