package kv;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InMemoryStorage {
    // using ConcurrentHashMap to store the data in memory.
    private ConcurrentHashMap<String, String> storage = new ConcurrentHashMap<>();
    // An hashmap to store the expiration times of the keys.
    private ConcurrentHashMap<String, Long> expirationTimes = new ConcurrentHashMap<>(); // this TTL is not saved as snapshot

    // Allocation one thread for the executor service.
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public void set(String key, String value) {
        storage.put(key, value);
    }

    // Method overloading for ttl.

    public void set(String key, String value, int ttl) {
        long expirationTime = System.currentTimeMillis() + ttl * 1000;
        storage.put(key, value);
        expirationTimes.put(key, expirationTime);

        executorService.schedule(() -> {
            storage.remove(key);
            expirationTimes.remove(key);
        }, ttl, TimeUnit.SECONDS);
    }

    public String get(String key) {
        return storage.get(key);
    }

    public void remove(String key) {
        storage.remove(key);
    }

    public boolean containsKey(String key) {
        return storage.containsKey(key);
    }

    public void clear() {
        storage.clear();
    }

    public String seeAll() {
        return storage.toString();
    }
    
    public String expire(String key, int ttl) {
        // tts is int as seconds 
        long expirationTime = System.currentTimeMillis() + ttl * 1000;

        expirationTimes.put(key, expirationTime);

        executorService.schedule(() -> {
            storage.remove(key);
        }, ttl, TimeUnit.SECONDS);
        return "OK";
    }

    public String TTL(String key) {
        if (expirationTimes.containsKey(key)) {
            // get expiration time of the key - current time
            long remainingTime = expirationTimes.get(key) - System.currentTimeMillis();
            return remainingTime > 0 ? remainingTime / 1000 + " seconds" : "Expired";
        } else {
            return "No TTL set";
        }
    }

    public String HELP() {
        String resetColor = "\u001B[0m";
        String commandColor = "\u001B[34m";
        String descriptionColor = "\u001B[37m";
    
        StringBuilder helpMessage = new StringBuilder();
    
        helpMessage.append(commandColor + "SET key value [ttl]" + resetColor + descriptionColor + " : Store the value with the specified key. Optional ttl (time to live) in seconds can be provided, after which the key-value pair will be automatically removed.\n" + resetColor);
        helpMessage.append(commandColor + "GET key" + resetColor + descriptionColor + " : Retrieve the value of the specified key.\n" + resetColor);
        helpMessage.append(commandColor + "REMOVE key" + resetColor + descriptionColor + " : Remove the key-value pair with the specified key.\n" + resetColor);
        helpMessage.append(commandColor + "CONTAINS key" + resetColor + descriptionColor + " : Check if the storage contains a value for the specified key.\n" + resetColor);
        helpMessage.append(commandColor + "CLEAR" + resetColor + descriptionColor + " : Remove all key-value pairs from the storage.\n" + resetColor);
        helpMessage.append(commandColor + "ALL" + resetColor + descriptionColor + " : See all key-value pairs in the storage.\n" + resetColor);
        helpMessage.append(commandColor + "EXPIRE key ttl" + resetColor + descriptionColor + " : Set a ttl (time to live) in seconds for the specified key, after which the key-value pair will be automatically removed.\n" + resetColor);
        helpMessage.append(commandColor + "TTL key" + resetColor + descriptionColor + " : Get the remaining time to live (in seconds) for the specified key.\n" + resetColor);
    
        return helpMessage.toString();
    }
}   