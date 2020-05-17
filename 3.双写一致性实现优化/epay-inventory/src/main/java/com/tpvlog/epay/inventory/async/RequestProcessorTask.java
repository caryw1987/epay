package com.tpvlog.epay.inventory.async;

import com.tpvlog.epay.inventory.request.ProductInventoryQueryRequest;
import com.tpvlog.epay.inventory.request.ProductInventoryUpdateRequest;
import com.tpvlog.epay.inventory.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求处理任务
 */
public class RequestProcessorTask implements Callable<Boolean> {
    private static final Logger log = LoggerFactory.getLogger(RequestProcessorTask.class);

    private final ArrayBlockingQueue<Request> queue;

    public RequestProcessorTask(ArrayBlockingQueue<Request> queue) {
        this.queue = queue;
    }

    @Override
    public Boolean call() {
        RequestQueue requestQueue = RequestQueue.getInstance();
        ConcurrentHashMap<Long, Boolean> filterMap = requestQueue.getFilterMap();
        while (true) {
            try {
                Request request = queue.take();
                Long productId = request.getProductId();
                log.info("工作线程处理请求: productId={}", productId);

                //1.写请求,直接处理
                if (request instanceof ProductInventoryUpdateRequest) {
                    filterMap.put(productId, true);
                    request.process();
                    continue;
                }

                // 2.读请求过滤
                if (request instanceof ProductInventoryQueryRequest) {
                    // 忽略过滤标识,强制刷新
                    if (request.isForceFresh()) {
                        request.process();
                    } else {
                        Boolean flag = filterMap.get(productId);
                        if (flag == null) { // 当前商品的第一个读请求
                            filterMap.put(productId, false);
                            request.process();
                        } else if (flag) {  // 已有写请求在队列中
                            filterMap.put(productId, false);
                            request.process();
                        } else {            // 已有读请求在队列中
                            ;
                        }
                    }
                }
            } catch (Exception ex) {
                log.error("工作线程处理异常", ex);
            }
        }
    }
}
