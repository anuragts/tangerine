package kv;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InMemoryStorage {
    // using ConcurrentHashMap to store the data in memory.
    private ConcurrentHashMap<String, String> storage = new ConcurrentHashMap<>();
    // Allocation one thread for the executor service.
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public void set(String key, String value) {
        storage.put(key, value);
    }

    // Method overloading for ttl.

    public void set(String key, String value, int ttl) {
        storage.put(key, value);

        executorService.schedule(() -> {
            storage.remove(key);
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
        executorService.schedule(() -> {
            storage.remove(key);
        }, ttl, TimeUnit.SECONDS);
        return "OK";
    }
}