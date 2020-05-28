package com.tpvlog.epay.cache.service.impl;

import com.alibaba.fastjson.JSON;
import com.tpvlog.epay.cache.dao.RedisDao;
import com.tpvlog.epay.cache.entity.ProductInfo;
import com.tpvlog.epay.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    private RedisDao redisDao;

    private static final String CACHE_NAME = "product";

    @CachePut(cacheNames = CACHE_NAME, key = "'key_'+#productInfo.getId()")
    @Override
    public ProductInfo updateLocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "'key_'+#id")
    @Override
    public ProductInfo queryLocalCache(Long id) {
        // 缓存中没有则返回null
        return null;
    }

    @Override
    public void updateReidsCache(ProductInfo productInfo) {
        redisDao.set("product_info_" + productInfo.getId(), JSON.toJSONString(productInfo));
    }

    @Override
    public ProductInfo queryReidsCache(Long id) {
        String data = redisDao.get("product_info_" + id);
        return JSON.parseObject(data, ProductInfo.class);
    }

    @Autowired
    public void setRedisDao(RedisDao redisDao) {
        this.redisDao = redisDao;
    }
}
