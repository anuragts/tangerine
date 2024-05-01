package vector;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Vector;

public class InMemoryVectorStorage {
    // Using ConcurrentHashMap to store the vectors in memory.
    private ConcurrentHashMap<String, Vector<Double>> storage = new ConcurrentHashMap<>();
    // An hashmap to store the expiration times of the keys.
    private ConcurrentHashMap<String, Long> expirationTimes = new ConcurrentHashMap<>();

    // Allocation one thread for the executor service.
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public void set(String key, Vector<Double> value) {
        storage.put(key, value);
    }

    // Method overloading for ttl.
    public void set(String key, Vector<Double> value, int ttl) {
        long expirationTime = System.currentTimeMillis() + ttl * 1000;
        storage.put(key, value);
        expirationTimes.put(key, expirationTime);

        executorService.schedule(() -> {
            storage.remove(key);
            expirationTimes.remove(key);
        }, ttl, TimeUnit.SECONDS);
    }

    public Vector<Double> get(String key) {
        return storage.get(key);
    }

    public String PING() {
        return "PONG";
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
        long expirationTime = System.currentTimeMillis() + ttl * 1000;

        expirationTimes.put(key, expirationTime);

        executorService.schedule(() -> {
            storage.remove(key);
        }, ttl, TimeUnit.SECONDS);
        return "OK";
    }

    public String TTL(String key) {
        if (expirationTimes.containsKey(key)) {
            long remainingTime = expirationTimes.get(key) - System.currentTimeMillis();
            return remainingTime > 0 ? remainingTime / 1000 + " seconds" : "Expired";
        } else {
            return "No TTL set";
        }
    }
}