package com.tpvlog.epay.cache.controller;

import com.tpvlog.epay.cache.entity.ProductInfo;
import com.tpvlog.epay.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CacheController {
    @Autowired
    private CacheService cacheService;

    @RequestMapping("/testUpdateCache")
    @ResponseBody
    public String testUpdateCache(ProductInfo productInfo) {
        cacheService.updateLocalCache(productInfo);
        return "success";
    }

    @RequestMapping("/testQueryCache/{id}")
    @ResponseBody
    public ProductInfo testGetCache(@PathVariable("id") Long id) {
        return cacheService.queryLocalCache(id);
    }

    /**
     * 获取商品信息
     *
     * @param productId
     * @return
     */
    @RequestMapping("/getProductInfo")
    @ResponseBody
    public ProductInfo getProductInfo(Long productId) {
        ProductInfo productInfo = null;

        productInfo = cacheService.getProductInfoFromReidsCache(productId);
        System.out.println("=================从redis中获取缓存，商品信息=" + productInfo);

        if (productInfo == null) {
            productInfo = cacheService.getProductInfoFromLocalCache(productId);
            System.out.println("=================从ehcache中获取缓存，商品信息=" + productInfo);
        }

        if (productInfo == null) {
            // 就需要从数据源重新拉去数据，重建缓存，但是这里先不讲
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
    public ShopInfo getShopInfo(Long shopId) {
        ShopInfo shopInfo = null;

        shopInfo = cacheService.getShopInfoFromReidsCache(shopId);
        System.out.println("=================从redis中获取缓存，店铺信息=" + shopInfo);

        if (shopInfo == null) {
            shopInfo = cacheService.getShopInfoFromLocalCache(shopId);
            System.out.println("=================从ehcache中获取缓存，店铺信息=" + shopInfo);
        }

        if (shopInfo == null) {
            // 就需要从数据源重新拉去数据，重建缓存，但是这里先不讲
        }

        return shopInfo;
    }
}
