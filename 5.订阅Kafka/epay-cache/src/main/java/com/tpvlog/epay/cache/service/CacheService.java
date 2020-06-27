package com.tpvlog.epay.cache.service;


import com.tpvlog.epay.cache.entity.ProductInfo;
import com.tpvlog.epay.cache.entity.ShopInfo;

/**
 * 缓存service接口
 *
 * @author resmix
 */
public interface CacheService {

    /**
     * 将商品基本信息保存到本地的ehcache缓存中
     *
     * @param productInfo
     */
    public void saveProductInfo2LocalCache(ProductInfo productInfo);

    /**
     * 从本地ehcache缓存中获取商品基本信息
     *
     * @param productId
     * @return
     */
    public ProductInfo getProductInfoFromLocalCache(Long productId);

    /**
     * 将店铺信息保存到本地的ehcache缓存中
     *
     * @param shopInfo
     */
    public void saveShopInfo2LocalCache(ShopInfo shopInfo);

    /**
     * 从本地ehcache缓存中获取店铺信息
     *
     * @param shopId
     * @return
     */
    public ShopInfo getShopInfoFromLocalCache(Long shopId);

    /**
     * 将商品基本信息保存到redis中
     *
     * @param productInfo
     */
    public void saveProductInfo2ReidsCache(ProductInfo productInfo);

    /**
     * 将店铺信息保存到redis中
     *
     * @param shopInfo
     */
    public void saveShopInfo2ReidsCache(ShopInfo shopInfo);

    /**
     * 从redis中获取商品信息
     *
     * @param productId
     */
    public ProductInfo getProductInfoFromReidsCache(Long productId);

    /**
     * 从redis中获取店铺信息
     *
     * @param shopId
     */
    public ShopInfo getShopInfoFromReidsCache(Long shopId);

    /**
     * 重建商品信息缓存
     *
     * @param productInfo
     */
    public void rebulidCache(ProductInfo productInfo);

    /**
     * 重建店铺信息缓存
     *
     * @param shopInfo
     */
    public void rebulidCache(ShopInfo shopInfo);
}