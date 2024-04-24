package tests;

import kv.InMemoryStorage;

public class KVTest {
    public static void main(String[] args) {
        test();
    }

    private static void test() {
        InMemoryStorage storage = new InMemoryStorage();
        storage.set("key1", "value1");
        storage.set("key1", "value1");
        storage.set("key1", "value1");
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
