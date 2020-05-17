package com.tpvlog.epay.inventory.controller;

import com.tpvlog.epay.inventory.entity.ProductInventory;
import com.tpvlog.epay.inventory.entity.vo.Response;
import com.tpvlog.epay.inventory.request.ProductInventoryQueryRequest;
import com.tpvlog.epay.inventory.request.ProductInventoryUpdateRequest;
import com.tpvlog.epay.inventory.request.Request;
import com.tpvlog.epay.inventory.service.ProductInventoryService;
import com.tpvlog.epay.inventory.service.RequestAsyncProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.locks.LockSupport;

@Controller
public class InventoryController {

    private static final Logger log = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    private ProductInventoryService inventoryService;

    @Autowired
    private RequestAsyncProcessorService requestAsyncProcessorService;

    @RequestMapping("/getInventory/{productId}")
    @ResponseBody
    public ProductInventory getInventory(@PathVariable("productId") Long productId) {
        log.info("getInventory:{}", productId);
        ProductInventory result = null;
        try {

            // 1.入队列，异步处理查询请求
            Request request = new ProductInventoryQueryRequest(new ProductInventory(productId, -1L), inventoryService, false);
            requestAsyncProcessorService.process(request);

            // 2.hang一段时间，尝试从缓存中查询（正常情况下，上面的读请求会将数据更新到缓存）
            long startTime = System.currentTimeMillis();
            long endTime = startTime;
            while (endTime - startTime < 120L) {
                result = inventoryService.queryCachedProductInventory(productId);
                if (result == null) {
                    // 等待20毫秒
                    LockSupport.parkNanos(20 * 1000 * 1000L);
                    endTime = System.currentTimeMillis();
                } else {
                    return result;
                }
            }

            // 3.缓存中查不到，直接从数据库查
            result = inventoryService.queryProductInventory(productId);
            if (result != null) {
                // 强制刷入缓存，因为缓存中的数据可能被Redis清除掉（Redis内存满而LRU清除），而队列中的filterMap状态一直为false
                requestAsyncProcessorService.process(new ProductInventoryQueryRequest(result,
                        inventoryService, true));
            }
        } catch (Exception ex) {
            log.error("getInventory failed", ex);
        }
        return result;
    }

    @RequestMapping("/updateInventory")
    @ResponseBody
    public Response updateInventory(ProductInventory productInventory) {
        log.info("updateInventory:{}", productInventory);
        Response response = null;
        try {
            // 封装更新请求
            Request request = new ProductInventoryUpdateRequest(productInventory, inventoryService);
            // 异步处理
            requestAsyncProcessorService.process(request);
            response = new Response(Response.SUCCESS);
        } catch (Exception e) {
            response = new Response(Response.FAILURE, e.getMessage());
        }
        return response;
    }
}
