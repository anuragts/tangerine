package kv;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PubSub {
    private Map<String, List<Subscriber>> subscribersMap = new HashMap<>();
    private Map<String, List<PrintWriter>> topicWriters = new HashMap<>();


    interface Subscriber {
        void receive(String topic, String message);
    }

    // Subscribes a Subscriber to a topic.
    public void subscribe(String topic, Subscriber subscriber,PrintWriter writer) {
        if (subscribersMap.containsKey(topic)) {
            subscribersMap.get(topic).add(subscriber);
        } else {
            List<Subscriber> subscribers = new ArrayList<>();
            subscribers.add(subscriber);
            subscribersMap.put(topic, subscribers);
        }

        if (!topicWriters.containsKey(topic)) {
            topicWriters.put(topic, new ArrayList<>());
        }
        topicWriters.get(topic).add(writer);
    }

    // Publishes a message to a topic.
    public void publish(String topic, String message) {
        if (!subscribersMap.containsKey(topic))
            return;
        subscribersMap.get(topic).forEach(subscriber -> subscriber.receive(topic, message));

        if (topicWriters.containsKey(topic)) {
            for (PrintWriter writer : topicWriters.get(topic)) {
                writer.println(message);
            }
        }
    }



    public void unsubscribe(String topic, Subscriber subscriber, PrintWriter writer) {
        if (!subscribersMap.containsKey(topic))
            return;
        subscribersMap.get(topic).remove(subscriber);

        if (topicWriters.containsKey(topic)) {
            topicWriters.get(topic).remove(writer);
        }
    }

}
