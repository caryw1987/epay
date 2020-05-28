package com.tpvlog.epay.cache.kafka;

import com.alibaba.fastjson.JSONObject;
import com.tpvlog.epay.cache.entity.ProductInfo;
import com.tpvlog.epay.cache.service.CacheService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CacheIntegrationConsumer {

    private CacheService cacheService;

    private static final Logger log = LoggerFactory.getLogger(CacheIntegrationConsumer.class);

    @KafkaListener(topics = {"product-topic"}, groupId = "product-group")
    public void consumeProduct(ConsumerRecord<Integer, String> record) {
        log.info("接收到商品服务通知: {}", record);
        String data = record.value();

        // 提取出商品id
        Long productId = Long.valueOf(data);

        // 调用商品信息服务的接口，生产环境一般RPC调用，这里直接注释模拟
        String productInfoJSON = "{\"id\": 1, \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1}";
        ProductInfo productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);

        // 更新本地缓存
        cacheService.updateLocalCache(productInfo);
        log.info("本地缓存的商品信息：{}", cacheService.queryLocalCache(productInfo.getId()));

        // 更新Redis缓存
        cacheService.updateReidsCache(productInfo);
        log.info("Redis缓存的商品信息：{}", cacheService.queryReidsCache(productInfo.getId()));
    }

    @Autowired
    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
}
