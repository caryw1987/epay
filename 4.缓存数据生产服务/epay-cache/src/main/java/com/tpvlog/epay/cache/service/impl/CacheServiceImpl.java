package com.tpvlog.epay.cache.service.impl;

import com.alibaba.fastjson.JSON;
import com.tpvlog.epay.cache.dao.RedisDao;
import com.tpvlog.epay.cache.entity.ProductInfo;
import com.tpvlog.epay.cache.entity.ShopInfo;
import com.tpvlog.epay.cache.service.CacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    @Autowired
    private RedisDao redisDao;

    public static final String CACHE_NAME = "local";
    public static final String PRODUCT_CACHE_PREFIX = "product_info_";
    public static final String SHOP_CACHE_PREFIX = "shop_info_";

    @CachePut(value = CACHE_NAME, key = PRODUCT_CACHE_PREFIX + " +#productInfo.getProductId()")
    @Override
    public void saveProductInfo2LocalCache(ProductInfo productInfo) {
    }

    @Cacheable(value = CACHE_NAME, key = PRODUCT_CACHE_PREFIX + " + #productId")
    @Override
    public ProductInfo getProductInfoFromLocalCache(Long productId) {
        // 没有缓存则返回null
        return null;
    }

    @CachePut(value = CACHE_NAME, key = SHOP_CACHE_PREFIX + "+#shopInfo.getShopId()")
    @Override
    public void saveShopInfo2LocalCache(ShopInfo shopInfo) {
    }

    @Cacheable(value = CACHE_NAME, key = SHOP_CACHE_PREFIX + "+#shopId")
    @Override
    public ShopInfo getShopInfoFromLocalCache(Long shopId) {
        return null;
    }

    @Override
    public void saveProductInfo2ReidsCache(ProductInfo productInfo) {
        String key = PRODUCT_CACHE_PREFIX + productInfo.getProductId();
        redisDao.set(key, JSON.toJSONString(productInfo));
    }

    @Override
    public void saveShopInfo2ReidsCache(ShopInfo shopInfo) {
        String key = SHOP_CACHE_PREFIX + shopInfo.getShopId();
        redisDao.set(key, JSON.toJSONString(shopInfo));
    }

    @Override
    public ProductInfo getProductInfoFromReidsCache(Long productId) {
        String key = PRODUCT_CACHE_PREFIX + productId;
        String data = redisDao.get(key);
        if (StringUtils.isBlank(data)) {
            return null;
        }
        return JSON.parseObject(data, ProductInfo.class);
    }

    @Override
    public ShopInfo getShopInfoFromReidsCache(Long shopId) {
        String key = PRODUCT_CACHE_PREFIX + shopId;
        String data = redisDao.get(key);
        if (StringUtils.isBlank(data)) {
            return null;
        }
        return JSON.parseObject(data, ShopInfo.class);
    }
}
