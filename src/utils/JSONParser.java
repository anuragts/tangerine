package utils;

import java.util.*;

public class JSONParser {
    public static Map<String, String> parseJSON(String jsonString) {
        Map<String, String> jsonMap = new HashMap<>();
        jsonString = jsonString.trim();
        if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
            jsonString = jsonString.substring(1, jsonString.length() - 1);
            String[] keyValuePairs = jsonString.split(",");
            for (String pair : keyValuePairs) {
                String[] entry = pair.split("[=:]");
                if (entry.length == 2) {
                    String key = entry[0].trim();
                    String value = entry[1].trim();
                    if (key.startsWith("\"") && key.endsWith("\"")) {
                        key = key.substring(1, key.length() - 1);
                    }
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                    }
                    jsonMap.put(key, value);
                }
            }
        }
        return jsonMap;
    }

    public static String convertToColonSyntax(String jsonString) {
        return jsonString.replace("=", ":");
    }

    public static String convertToEqualSyntax(String jsonString) {
        return jsonString.replace(":", "=");
    }

    public static String toString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder("{");
        for (String key : map.keySet()) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(key).append(":").append(map.get(key));   
        }
        sb.append("}");
        return sb.toString();
    }

    public static void main(String[] args) {
        String jsonString = "{chota:bheem, key=value, raj=shah}";
        Map<String, String> parsedJSON = parseJSON(jsonString);
        System.out.println("Parsed JSON: " + toString(parsedJSON));
        for (String key : parsedJSON.keySet()) {
            System.out.println("Key: " + key + ", Value: " + parsedJSON.get(key));
        }
        System.out.println(toString(parsedJSON));
    }
}