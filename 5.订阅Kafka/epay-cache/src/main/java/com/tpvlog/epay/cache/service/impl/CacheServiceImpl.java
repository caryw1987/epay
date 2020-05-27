package com.tpvlog.epay.cache.service.impl;

import com.tpvlog.epay.cache.entity.ProductInfo;
import com.tpvlog.epay.cache.service.CacheService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service("cacheService")
public class CacheServiceImpl implements CacheService {

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
}
