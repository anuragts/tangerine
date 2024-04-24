package kv;

import utils.JSONParser;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SnapShot {

    // Specify your directory and filename here
    private final String directory = "snapshots/";
    private final String filename = "snapshot.json";

    public void saveToJSON(Map<String, String> data) {
        // Convert the map to a JSON string
        String json = JSONParser.toString(data);

        try {
            // Create directory if it doesn't exist
            Files.createDirectories(Paths.get(directory));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Check if the existing snapshot is the same as the new data
        Map<String, String> existingSnapshot = readSnapshot();
        if(existingSnapshot.equals(data)) {
            System.out.println("No changes detected in snapshot data. Skipping write operation.");
            return;
        }

        // Write JSON string to file
        try (FileWriter file = new FileWriter(directory + filename)) {
            file.write(json);
            System.out.println("Successfully saved data to " + directory + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> readSnapshot() {
        try {
            String content = Files.lines(Paths.get(directory + filename)).collect(Collectors.joining());
            return JSONParser.parseJSON(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public static void main(String[] args) {
        // Create an instance of SnapShot
        SnapShot snapShot = new SnapShot();

        // Create a map with your data
        Map<String, String> data = new HashMap<>();
        data.put("chota", "bheem");
        data.put("key", "value");
        data.put("raj", "shah");

        // Use the saveToJSON method
        snapShot.saveToJSON(data);
        System.out.println("read");
        System.out.println(snapShot.readSnapshot());
    }
}