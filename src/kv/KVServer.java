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
        this.serverSocket = new ServerSocket(port); // create a new server socket
        this.executor = Executors.newFixedThreadPool(10); // fixed no of threads
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
            File[] snapshotFiles = snapshotFile.listFiles();
            if (snapshotFiles != null) {
                for (File file : snapshotFiles) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split("= ");
                            storage.set(parts[0], parts[1]);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private void handleRequest(Socket socket) throws IOException {
        // get input stream and output stream from the socket and create a buffered
        // reader and a writer.
        // SnapShot snapShot = new SnapShot();
        GlobSnapShot globSnapShot = new GlobSnapShot();
        JSONParser parser = new JSONParser();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            String input;
            while ((input = reader.readLine()) != null) {
                String[] parts = input.split(" ");
                if (parts[0].equals("set")) {
                    storage.set(parts[1], parts[2]);
                    globSnapShot.saveToGlob(parser.parseJSON(storage.seeAll()));
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
                    globSnapShot.saveToGlob(parser.parseJSON(storage.seeAll()));
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
            // save snapshot every time a command is executed
            // snapShot.saveToJSON(d);

        }
    }



    public static void main(String[] args) throws IOException {
        InMemoryStorage storage = new InMemoryStorage();
        int port = 1111;
        KVServer server = new KVServer(storage, port);
        server.run();
    }
}