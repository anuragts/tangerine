package kv;

import java.util.*;

public class PubSub {
    private Map<String, List<Subscriber>> subscribers = new HashMap<>();

    public void subscribe(String topic, Subscriber subscriber) {
        if (!subscribers.containsKey(topic)) {
            subscribers.put(topic, new ArrayList<>());
        }
        subscribers.get(topic).add(subscriber);
    }

    public void unsubscribe(String topic, Subscriber subscriber) {
        if (subscribers.containsKey(topic)) {
            subscribers.get(topic).remove(subscriber);
        }
    }

    public void publish(String topic, String message) {
        if (subscribers.containsKey(topic)) {
            for (Subscriber subscriber : subscribers.get(topic)) {
                subscriber.receive(message);
            }
        }
    }
    
    public interface Subscriber {
        void receive(String message);
    }

    public static void main(String[] args) {
        PubSub pubSub = new PubSub();

        PubSub.Subscriber subscriber1 = System.out::println;
        PubSub.Subscriber subscriber2 = System.out::println;
        PubSub.Subscriber subscriber3 = System.out::println;

        pubSub.subscribe("topic1", subscriber1);
        pubSub.subscribe("topic1", subscriber2);
        pubSub.subscribe("topic2", subscriber3);

        pubSub.publish("topic1", "Hello, Topic 1!");
        pubSub.publish("topic2", "Hello, Topic 2!");

        pubSub.unsubscribe("topic1", subscriber2);

        pubSub.publish("topic1", "Goodbye, Topic 1!");
    }
}