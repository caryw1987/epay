package com.tpvlog.epay.inventory.async;

import com.tpvlog.epay.inventory.request.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求队列
 *
 * @author ressmix
 */
public class RequestQueue {

    private static final RequestQueue instance = new RequestQueue();

    /**
     * KEY: 商品ID
     * VALUE: true-该商品的队列中已有一个写请求；false-该商品的队列中已有一个读请求; null-该商品的队列为空
     */
    private static final ConcurrentHashMap<Long, Boolean> filterMap = new ConcurrentHashMap<Long, Boolean>();

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

    public ConcurrentHashMap<Long, Boolean> getFilterMap() {
        return filterMap;
    }
}
