package kv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Flow.Subscriber;

public class PubSub {
    private Map<String, List<Subscriber>> subscribersMap = new HashMap<>();

    interface Subscriber {
        void receive(String topic, String message);
    }

    // Subscribes a Subscriber to a topic.
    public void subscribe(String topic, Subscriber subscriber) {
        if (subscribersMap.containsKey(topic)) {
            subscribersMap.get(topic).add(subscriber);
        } else {
            List<Subscriber> subscribers = new ArrayList<>();
            subscribers.add(subscriber);
            subscribersMap.put(topic, subscribers);
        }
    }

    // Publishes a message to a topic.
    public void publish(String topic, String message) {
        if (!subscribersMap.containsKey(topic))
            return;
        subscribersMap.get(topic).forEach(subscriber -> subscriber.receive(topic, message));
    }

    public void unsubscribe(String topic, Subscriber subscriber) {
        if (!subscribersMap.containsKey(topic))
            return;
        subscribersMap.get(topic).remove(subscriber);
    }

}
