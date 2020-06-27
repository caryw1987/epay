package com.tpvlog.epay.cache.controller;

import com.tpvlog.epay.cache.entity.ProductInfo;
import com.tpvlog.epay.cache.entity.ShopInfo;
import com.tpvlog.epay.cache.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CacheController {

    @Autowired
    private CacheService cacheService;

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
            // TODO: 从源服务请求数据，重建缓存，这里先不讲
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
            // TODO: 从源服务请求数据，重建缓存，这里先不讲
        }

        return shopInfo;
    }
}
