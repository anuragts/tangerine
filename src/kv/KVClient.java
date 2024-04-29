package kv;

import java.io.*;
import java.net.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class KVClient {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    // Synchronizing both threads so client main thread can wait for response from server.
    private Lock lock = new ReentrantLock();
    private Condition receivedResponse = lock.newCondition();
  
    public KVClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  
        // thread listens for events from server. Is asynchronous in nature.
        new Thread(() -> {
            String response;
            try {
                while ((response = reader.readLine()) != null) {
                    System.out.println(response);
                    lock.lock();
                    try {
                        receivedResponse.signalAll();
                    } finally {
                        lock.unlock();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendCommand(String command) throws InterruptedException {
        // main thread is locked
        lock.lock();
        try {
            writer.println(command);
            // awaiting response from server
            receivedResponse.await();
        } finally {
            // main thread is unlocked
            lock.unlock();
        }
    }

    public void close() throws IOException {
        socket.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
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