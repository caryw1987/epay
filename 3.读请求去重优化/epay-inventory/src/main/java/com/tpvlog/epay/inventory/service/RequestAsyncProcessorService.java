package com.tpvlog.epay.inventory.service;

import com.tpvlog.epay.inventory.request.Request;

/**
 * 请求异步处理服务
 *
 * @author ressmix
 */
public interface RequestAsyncProcessorService {
    void process(Request request);
}
