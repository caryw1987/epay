package com.tpvlog.epay.inventory.async;

import com.tpvlog.epay.inventory.request.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 请求队列
 *
 * @author ressmix
 */
public class RequestQueue {

    private static final RequestQueue instance = new RequestQueue();

    /**
     * 内存队列
     */
    private final List<ArrayBlockingQueue<Request>> queues = new ArrayList<ArrayBlockingQueue<Request>>();

    private RequestQueue() {
    }

    public static RequestQueue getInstance() {
        return instance;
    }

    /**
     * 添加一个内存队列
     *
     * @param queue
     */
    public void addQueue(ArrayBlockingQueue<Request> queue) {
        this.queues.add(queue);
    }

    /**
     * 获取一个内存队列
     */
    public ArrayBlockingQueue<Request> getQueue(int idx) {
        return this.queues.get(idx);
    }

    public int size() {
        return queues.size();
    }
}
