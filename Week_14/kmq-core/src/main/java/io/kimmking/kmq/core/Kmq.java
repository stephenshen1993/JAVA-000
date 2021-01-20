package io.kimmking.kmq.core;

import io.kimmking.kmq.queue.MyQueue;
import lombok.SneakyThrows;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public final class Kmq {

    public Kmq(String topic, int capacity) {
        this.topic = topic;
        this.capacity = capacity;
        this.queue = new MyQueue(capacity);
    }

    private String topic;

    private int capacity;

    private MyQueue<KmqMessage> queue;

    public boolean send(KmqMessage message) {
        return queue.offer(message);
    }

    @SneakyThrows
    public KmqMessage poll(long timeout) {
        return queue.poll(timeout, TimeUnit.MILLISECONDS);
    }

}
