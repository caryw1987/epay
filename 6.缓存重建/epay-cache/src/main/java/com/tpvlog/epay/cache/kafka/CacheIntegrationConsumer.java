package com.tpvlog.epay.cache.kafka;

import com.alibaba.fastjson.JSONObject;
import com.tpvlog.epay.cache.entity.ProductInfo;
import com.tpvlog.epay.cache.entity.ShopInfo;
import com.tpvlog.epay.cache.service.CacheService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CacheIntegrationConsumer {

    @Autowired
    private CacheService cacheService;

    private static final Logger LOG = LoggerFactory.getLogger(CacheIntegrationConsumer.class);

    @KafkaListener(topics = {"product-topic"}, groupId = "product-group")
    public void consumeProduct(ConsumerRecord<Integer, String> record) {
        LOG.info("接收到商品服务通知: {}", record);
        String data = record.value();

        // 提取出商品id
        Long productId = Long.valueOf(data);

        // 调用商品基本信息服务的接口，获取最新数据
        // 生产环境一般RPC调用，这里直接注释模拟
        String productInfoJSON = "{\"id\": 1,\"productId\": " + productId + ", \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 15, \"modifiedTime\": \"2020-01-01 12:00:00\"}";
        ProductInfo productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);

        // 重建本地缓存和Redis缓存
        cacheService.rebulidCache(productInfo);
    }

    @KafkaListener(topics = {"shop-topic"}, groupId = "shop-group")
    public void consumeShop(ConsumerRecord<Integer, String> record) {
        LOG.info("接收到店铺服务通知: {}", record);
        String data = record.value();

        // 提取出店铺id
        Long shopId =  Long.valueOf(data);

        // 调用店铺服务的接口
        // 生产环境一般RPC调用，这里直接注释模拟
        String shopInfoJSON = "{\"id\": 1,\"shopId\": " + shopId + ", \"name\": \"小王的手机店\", \"level\": 5, \"goodCommentRate\":0.99, \"modifiedTime\": \"2020-01-01 13:00:00\"}";
        ShopInfo shopInfo = JSONObject.parseObject(shopInfoJSON, ShopInfo.class);

        // 重建本地缓存和Redis缓存
        cacheService.rebulidCache(shopInfo);
    }
}
