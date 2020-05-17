package com.tpvlog.epay.inventory.async;

import com.tpvlog.epay.inventory.request.Request;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**
 * 请求处理任务
 */
public class RequestProcessorTask implements Callable<Boolean> {

    private final ArrayBlockingQueue<Request> queue;

    public RequestProcessorTask(ArrayBlockingQueue<Request> queue) {
        this.queue = queue;
    }

    @Override
    public Boolean call() throws Exception {
        while (true) {
            try {
                Request request = queue.take();
                request.process();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
