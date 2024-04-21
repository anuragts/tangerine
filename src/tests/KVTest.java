package tests;

import kv.InMemoryStorage;

public class KVTest {
    public static void main(String[] args) {
        test();
    }

    private static void test() {
        InMemoryStorage storage = new InMemoryStorage();
        storage.put("key1", "value1");
        storage.put("key2", "value2");
        storage.put("key3", "value3");
        System.out.println(storage.seeAll());
        System.out.println(storage.get("key1"));
        System.out.println(storage.get("key2"));
        System.out.println(storage.get("key3"));
        storage.remove("key1");
        System.out.println(storage.seeAll());
        storage.clear();
        System.out.println(storage.seeAll());
    }
}
