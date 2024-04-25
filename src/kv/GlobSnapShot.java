package kv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.nio.file.StandardOpenOption;

public class GlobSnapShot {

    // Specify your directory and filename here
    private final String directory = "snapshots/";
    private final String filename = "snapshot.glob";

    public void saveToGlob(Map<String, String> data) {
        try {
            // Create directory if it doesn't exist
            Files.createDirectories(Paths.get(directory));

            // Read existing data
            Map<String, String> existingData = readSnapshot();

            // Append new data to file
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(directory + filename),
                    StandardOpenOption.APPEND)) {
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    // Only append entries that don't already exist in the file
                    // or have a different value
                    if (!existingData.containsKey(key) || !existingData.get(key).equals(value)) {
                        writer.write(key + "=" + value);
                        writer.newLine();
                    }
                }
            }

            System.out.println("Successfully saved data to " + directory + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> readSnapshot() {
        Map<String, String> data = new HashMap<>();
        Path path = Paths.get(directory + filename);
    
        try {
            // Create file if it doesn't exist
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
    
            try (Stream<String> lines = Files.lines(path)) {
                lines.forEach(line -> {
                    String[] parts = line.split("=", 2);
                    if (parts.length >= 2) {
                        String key = parts[0];
                        String value = parts[1];
                        data.put(key, value);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return data;
    }

    public static void main(String[] args) {
        // Create an instance of SnapShot
        GlobSnapShot globSnapShot = new GlobSnapShot();

        // Create a map with your data
        Map<String, String> data = new HashMap<>();
        // data.put("openai", "gpt3");
        data.put("anthropic", "claude");

        // Use the saveToGlob method 
        globSnapShot.saveToGlob(data);

        // Use the readSnapshot method
        System.out.println(globSnapShot.readSnapshot());
    }
}