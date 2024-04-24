package kv;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorage {
    private ConcurrentHashMap<String, String> storage = new ConcurrentHashMap<>();

    public void set(String key, String value) {
        storage.put(key, value);
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

    public static void main(String[] args) {
    }
}