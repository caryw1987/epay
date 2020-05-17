package com.tpvlog.epay.inventory.service.impl;

import com.tpvlog.epay.inventory.async.RequestQueue;
import com.tpvlog.epay.inventory.request.Request;
import com.tpvlog.epay.inventory.service.RequestAsyncProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 商品路由服务
 *
 * @author ressmix
 */
@Service("requestAsyncProcessorService")
public class RequestAsyncProcessorServiceImpl implements RequestAsyncProcessorService {
    private static final Logger log = LoggerFactory.getLogger(RequestAsyncProcessorServiceImpl.class);

    @Override
    public void process(Request request) {
        try {
            Long productId = request.getProductId();
            ArrayBlockingQueue<Request> queue = getRoutingQueue(productId);
            queue.put(request);
        } catch (Exception ex) {
            log.error("处理异步请求失败:{}", request, ex);
        }
    }

    /**
     * 获取路由到的内存队列
     *
     * @param productId 商品id
     * @return 内存队列
     */
    private ArrayBlockingQueue<Request> getRoutingQueue(Long productId) {
        RequestQueue requestQueue = RequestQueue.getInstance();

        // 获取productId的hash值
        int hash = String.valueOf(productId).hashCode();
        hash = hash ^ (hash >>> 16);

        //  获取队列
        int index = (requestQueue.size() - 1) & hash;
        return requestQueue.getQueue(index);
    }
}
