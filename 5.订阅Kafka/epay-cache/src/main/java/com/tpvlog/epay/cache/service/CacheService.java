package com.tpvlog.epay.cache.service;


import com.tpvlog.epay.cache.entity.ProductInfo;

/**
 * 缓存service接口
 *
 * @author resmix
 */
public interface CacheService {

    /**
     * 将商品信息保存到本地缓存中
     *
     * @param productInfo
     * @return
     */
    ProductInfo updateLocalCache(ProductInfo productInfo);

    /**
     * 从本地缓存中获取商品信息
     *
     * @param id
     * @return
     */
    ProductInfo queryLocalCache(Long id);
}