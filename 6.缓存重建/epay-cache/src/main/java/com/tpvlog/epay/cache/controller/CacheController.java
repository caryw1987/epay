package com.tpvlog.epay.cache.controller;

import com.alibaba.fastjson.JSONObject;
import com.tpvlog.epay.cache.entity.ProductInfo;
import com.tpvlog.epay.cache.entity.ShopInfo;
import com.tpvlog.epay.cache.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CacheController {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(CacheController.class);

    /**
     * 获取商品基本信息
     *
     * @param productId
     * @return
     */
    @RequestMapping("/getProductInfo")
    @ResponseBody
    public ProductInfo getProductInfo(@RequestParam("productId") Long productId) {
        ProductInfo productInfo = null;

        // 1.从Redis查找商品基本信息
        productInfo = cacheService.getProductInfoFromReidsCache(productId);
        LOG.info("=================从redis中获取缓存，商品信息=" + productInfo);

        if (productInfo == null) {
            // 2.从本地JVM缓存查找
            productInfo = cacheService.getProductInfoFromLocalCache(productId);
            LOG.info("=================从ehcache中获取缓存，商品信息=" + productInfo);
        }

        if (productInfo == null) {
            // 从源服务请求数据，这里直接模拟
            String productInfoJSON = "{\"id\": 2,\"productId\": 1666, \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 16, \"modifiedTime\": \"2020-01-01 18:00:00\"}";
            productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);

            // 异步重建缓存
            try {
                kafkaTemplate.send("product-topic", String.valueOf(productId));
            } catch (Exception ex) {
                LOG.error("发送重建缓存消息失败", ex);
            }
        }

        return productInfo;
    }

    /**
     * 获取店铺信息
     *
     * @param shopId
     * @return
     */
    @RequestMapping("/getShopInfo")
    @ResponseBody
    public ShopInfo getShopInfo(@RequestParam("shopId") Long shopId) {
        ShopInfo shopInfo = null;

        // 1.从Redis查找店铺信息
        shopInfo = cacheService.getShopInfoFromReidsCache(shopId);
        LOG.info("=================从redis中获取缓存，店铺信息=" + shopInfo);

        if (shopInfo == null) {
            // 2.从本地JVM缓存查找
            shopInfo = cacheService.getShopInfoFromLocalCache(shopId);
            LOG.info("=================从ehcache中获取缓存，店铺信息=" + shopInfo);
        }

        if (shopInfo == null) {
            // 从源服务请求数据，这里直接模拟
            String shopInfoJSON = "{\"id\": 2,\"shopId\": 16, \"name\": \"小王的手机店\", \"level\": 5, \"goodCommentRate\":0.99, \"modifiedTime\": \"2020-01-01 18:00:00\"}";
            shopInfo = JSONObject.parseObject(shopInfoJSON, ShopInfo.class);

            // 异步重建缓存
            try {
                kafkaTemplate.send("shop-topic", String.valueOf(shopId));
            } catch (Exception ex) {
                LOG.error("发送重建缓存消息失败", ex);
            }

        }

        return shopInfo;
    }
}
