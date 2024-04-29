package kv;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import utils.*;

public class KVServer {
    private InMemoryStorage storage;
    private ServerSocket serverSocket;
    private ExecutorService executor;
    private File snapshotFile;
    private ScheduledExecutorService snapshotExecutor = Executors.newSingleThreadScheduledExecutor();
    private PubSub pubSub = new PubSub(); // Instance of our PubSub class

    public KVServer(InMemoryStorage storage, int port) throws IOException {
        this.storage = storage;
        this.serverSocket = new ServerSocket(port);
        this.executor = Executors.newFixedThreadPool(3); // fixed no of threads (can be increased or decreased according
                                                         // to need)

        GlobSnapShot globSnapShot = new GlobSnapShot();

        System.out.println("KV Server started on port " + port);
        snapshotFile = new File("snapshots");
        snapshotFile.mkdirs();

        // Schedule the snapshot task to run every 120 seconds
        snapshotExecutor.scheduleAtFixedRate(() -> globSnapShot.saveToGlob(JSONParser.parseJSON(storage.seeAll())), 0,
                20, TimeUnit.SECONDS);
        // Initialize GlobSnapShot and load the existing data
        Map<String, String> existingData = globSnapShot.readSnapshot();
        for (Map.Entry<String, String> entry : existingData.entrySet()) {
            storage.set(entry.getKey(), entry.getValue());
        }

        // Add graceful shutdown to handle the server shutdown and take snapshot of the
        // data.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server...");
            try {
                globSnapShot.saveToGlob(JSONParser.parseJSON(storage.seeAll()));
                serverSocket.close(); // Close the socket connection
                executor.shutdownNow(); // Attempts to stop all actively executing tasks
                snapshotExecutor.shutdownNow(); // Attempts to stop all actively executing tasks
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

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
                    case "PING":
                        writer.println(storage.PING());
                        writer.println();
                        break;
                    case "SET":
                        // method overloading if ttl (time to live) is provided
                        // ttl is in seconds.
                        if (parts.length == 4) {
                            int ttl = Integer.parseInt(parts[3]);
                            storage.set(parts[1], parts[2], ttl);
                            System.out.println("TTL set to " + ttl);
                        } else {
                            storage.set(parts[1], parts[2]);
                        }
                        writer.println("OK");
                        writer.println();
                        break;
                    case "GET":
                        String value = storage.get(parts[1]);
                        if (value != null) {
                            writer.println(value);
                            writer.println();
                        } else {
                            writer.println("NOT_FOUND");
                            writer.println();
                        }
                        break;
                    case "REMOVE":
                        storage.remove(parts[1]);
                        globSnapShot.saveToGlob(JSONParser.parseJSON(storage.seeAll()));
                        writer.println("OK");
                        writer.println();
                        break;
                    case "CONTAINS":
                        boolean contains = storage.containsKey(parts[1]);
                        writer.println(contains ? "TRUE" : "FALSE");
                        writer.println();
                        break;
                    case "CLEAR":
                        storage.clear();
                        writer.println("OK");
                        writer.println();
                        break;
                    case "ALL":
                        writer.println(storage.seeAll());
                        writer.println();
                        break;
                    case "EXPIRE":
                        String expire = storage.expire(parts[1], Integer.parseInt(parts[2]));
                        writer.println(expire);
                        writer.println();
                        break;
                    case "TTL":
                        String ttl = storage.TTL(parts[1]);
                        writer.println(ttl);
                        writer.println();
                        break;
                    case "SUBSCRIBE":
                        pubSub.subscribe(parts[1], message -> writer.println("Received: " + message));
                        writer.println("OK");
                        writer.println();
                        break;
                    case "UNSUBSCRIBE":
                        pubSub.unsubscribe(parts[1], message -> writer.println("Received: " + message));
                        writer.println("OK");
                        writer.println();
                        break;
                    case "PUBLISH":
                        pubSub.publish(parts[1], parts[2]);
                        writer.println("OK");
                        writer.println();
                        break;
                    case "HELP":
                        writer.println(storage.HELP());
                        writer.println();
                        break;
                    default:
                        writer.println("Unknown command");
                        writer.println();
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