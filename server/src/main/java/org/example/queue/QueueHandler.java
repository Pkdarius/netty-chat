package org.example.queue;

import org.example.dto.RequestWrapper;

import java.util.concurrent.*;

public class QueueHandler implements Runnable {

    private int corePoolSize = 10;
    private int maximumPoolSize = 100;

    public QueueHandler(Integer corePoolSize, Integer maximumPoolSize) {
        if (corePoolSize != null) {
            this.corePoolSize = corePoolSize;
        }
        if (maximumPoolSize != null) {
            this.maximumPoolSize = maximumPoolSize;
        }
        System.out.printf("Thread pool: core=%d, max=%d\n", corePoolSize, maximumPoolSize);
    }

    @Override
    public void run() {
        var messageQueue = MessageQueue.getInstance();
        var blockQueueRunnable = new ArrayBlockingQueue<Runnable>(100);
        var threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,1L, TimeUnit.MINUTES, blockQueueRunnable);

        while (true) {
            RequestWrapper requestWrapper = messageQueue.take();
            if (requestWrapper != null) {
                threadPoolExecutor.execute(new RequestHandler(requestWrapper));
            }
        }
    }
}
