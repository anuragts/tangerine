package kv;

import java.util.Scanner;

public class KVClient {
    private InMemoryStorage storage;

    public KVClient(InMemoryStorage storage) {
        this.storage = storage;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("tangerine kv> ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            if (parts[0].equals("put")) {
                storage.put(parts[1], parts[2]);
                System.out.println("OK");
            } else if (parts[0].equals("get")) {
                String value = storage.get(parts[1]);
                if (value != null) {
                    System.out.println(value);
                } else {
                    System.out.println("NOT_FOUND");
                }
            } else if (parts[0].equals("remove")) {
                storage.remove(parts[1]);
                System.out.println("OK");
            } else if (parts[0].equals("contains")) {
                boolean contains = storage.containsKey(parts[1]);
                System.out.println(contains ? "TRUE" : "FALSE");
            } else if (parts[0].equals("clear")) {
                storage.clear();
                System.out.println("OK");
            } else if (parts[0].equals("seeAll")) {
                System.out.println(storage.seeAll());
            } else if (parts[0].equals("exit")) {
                break;
            } else {
                System.out.println("Unknown command");
            }
        }
        scanner.close();
    }

    public static void main(String[] args) {
        InMemoryStorage storage = new InMemoryStorage();
        KVClient client = new KVClient(storage);
        client.run();
    }
}