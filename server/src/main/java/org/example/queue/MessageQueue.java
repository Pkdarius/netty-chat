package org.example.queue;

import org.example.dto.RequestWrapper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class MessageQueue {
    private volatile static MessageQueue instance;
    private final BlockingQueue<RequestWrapper> queue;

    private MessageQueue() {
        queue = new PriorityBlockingQueue<>();
    }

    public static MessageQueue getInstance() {
        if (instance == null) {
            synchronized (MessageQueue.class) {
                if (instance == null) {
                    instance = new MessageQueue();
                }
            }
        }
        return instance;
    }

    public void put(RequestWrapper request) {
        try {
            queue.put(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public RequestWrapper take() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
