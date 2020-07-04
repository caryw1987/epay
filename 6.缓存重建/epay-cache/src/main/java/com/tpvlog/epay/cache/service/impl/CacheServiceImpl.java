package com.tpvlog.epay.cache.service.impl;

import com.alibaba.fastjson.JSON;
import com.tpvlog.epay.cache.dao.RedisDao;
import com.tpvlog.epay.cache.entity.ProductInfo;
import com.tpvlog.epay.cache.entity.ShopInfo;
import com.tpvlog.epay.cache.enums.LockType;
import com.tpvlog.epay.cache.service.CacheService;
import com.tpvlog.epay.cache.zk.ZooKeeperLock;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    private static final Logger LOG = LoggerFactory.getLogger(CacheServiceImpl.class);

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private ZooKeeperLock zooKeeperLock;


    @CachePut(value = "local", key = "'product_info_' + #productInfo.getProductId()")
    @Override
    public void saveProductInfo2LocalCache(ProductInfo productInfo) {
    }

    @Cacheable(value = "local", key = "'product_info_'+ #productId")
    @Override
    public ProductInfo getProductInfoFromLocalCache(Long productId) {
        // 没有缓存则返回null
        return null;
    }

    @CachePut(value = "local", key = "'shop_info_'+#shopInfo.getShopId()")
    @Override
    public void saveShopInfo2LocalCache(ShopInfo shopInfo) {
    }

    @Cacheable(value = "local", key = "'shop_info_'+#shopId")
    @Override
    public ShopInfo getShopInfoFromLocalCache(Long shopId) {
        return null;
    }

    @Override
    public void saveProductInfo2ReidsCache(ProductInfo productInfo) {
        String key = "product_info_" + productInfo.getProductId();
        redisDao.set(key, JSON.toJSONString(productInfo));
    }

    @Override
    public void saveShopInfo2ReidsCache(ShopInfo shopInfo) {
        String key = "shop_info_" + shopInfo.getShopId();
        redisDao.set(key, JSON.toJSONString(shopInfo));
    }

    @Override
    public ProductInfo getProductInfoFromReidsCache(Long productId) {
        String key = "product_info_" + productId;
        String data = redisDao.get(key);
        if (StringUtils.isBlank(data)) {
            return null;
        }
        return JSON.parseObject(data, ProductInfo.class);
    }

    @Override
    public ShopInfo getShopInfoFromReidsCache(Long shopId) {
        String key = "shop_info_" + shopId;
        String data = redisDao.get(key);
        if (StringUtils.isBlank(data)) {
            return null;
        }
        return JSON.parseObject(data, ShopInfo.class);
    }

    @Override
    public void rebulidCache(ProductInfo productInfo) {
        // 获取分布式锁
        InterProcessMutex lock = zooKeeperLock.getMutex(LockType.PRODUCT, String.valueOf(productInfo.getProductId()));
        boolean isLocked = false;
        try {
            // 超时10s
            isLocked = lock.acquire(10000, TimeUnit.MILLISECONDS);
            if (isLocked) {
                LOG.info("{} 获取分布式锁成功", Thread.currentThread().getName());
                //获取成功
                doRebulidCache(productInfo);
            } else {
                LOG.error("尝试获取分布式锁失败，productId={}", productInfo.getProductId());
            }
        } catch (Exception e) {
            LOG.error("获取分布式锁异常，productId={}", productInfo.getProductId(), e);
        } finally {
            if (isLocked) {
                try {
                    lock.release();
                } catch (Exception e) {
                    LOG.error("释放分布式锁异常，productId={}", productInfo.getProductId(), e);
                }
            }
        }
    }

    private void doRebulidCache(ProductInfo productInfo) {
        ProductInfo redisCache = getProductInfoFromReidsCache(productInfo.getProductId());
        ProductInfo jvmCache = getProductInfoFromLocalCache(productInfo.getProductId());

        // 重建Redis缓存
        if (validateDate(productInfo, redisCache)) {
            saveProductInfo2ReidsCache(productInfo);
        }

        // 重建JVM缓存
        if (validateDate(productInfo, jvmCache)) {
            saveProductInfo2LocalCache(productInfo);
        }
    }

    private boolean validateDate(ProductInfo newData, ProductInfo cacheData) {
        if (cacheData == null) {
            return true;
        }

        // 比较当前数据的时间版本比已有数据的时间版本是新还是旧
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(newData.getModifiedTime());
            Date existedDate = sdf.parse(cacheData.getModifiedTime());

            if (date.before(existedDate)) {
                LOG.info("待更新的数据日期[{}]早于缓存中的数据日期[{}]，不进行更新",
                        newData.getModifiedTime(), cacheData.getModifiedTime());
                return false;
            }
        } catch (Exception e) {
            LOG.info("解析数据日期异常", e);
            return false;
        }

        return true;
    }

    private boolean validateDate(ShopInfo newData, ShopInfo cacheData) {
        if (cacheData == null) {
            return true;
        }

        // 比较当前数据的时间版本比已有数据的时间版本是新还是旧
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(newData.getModifiedTime());
            Date existedDate = sdf.parse(cacheData.getModifiedTime());

            if (date.before(existedDate)) {
                LOG.info("待更新的数据日期[{}]早于缓存中的数据日期[{}]，不进行更新",
                        newData.getModifiedTime(), cacheData.getModifiedTime());
                return false;
            }
        } catch (Exception e) {
            LOG.info("解析数据日期异常", e);
            return false;
        }

        return true;
    }

    @Override
    public void rebulidCache(ShopInfo shopInfo) {
        // 获取分布式锁
        InterProcessMutex lock = zooKeeperLock.getMutex(LockType.SHOP, String.valueOf(shopInfo.getShopId()));
        boolean isLocked = false;
        try {
            // 超时10s
            isLocked = lock.acquire(10000, TimeUnit.MILLISECONDS);
            if (isLocked) {
                //获取成功
                doRebulidCache(shopInfo);
            } else {
                LOG.error("尝试获取分布式锁失败，shopId={}", shopInfo.getShopId());
            }
        } catch (Exception e) {
            LOG.error("获取分布式锁异常，shopId={}", shopInfo.getShopId(), e);
        } finally {
            if (isLocked) {
                try {
                    lock.release();
                } catch (Exception e) {
                    LOG.error("释放分布式锁异常，shopId={}", shopInfo.getShopId(), e);
                }
            }
        }
    }

    private void doRebulidCache(ShopInfo shopInfo) {
        ShopInfo redisCache = getShopInfoFromReidsCache(shopInfo.getShopId());
        ShopInfo jvmCache = getShopInfoFromLocalCache(shopInfo.getShopId());

        // 重建Redis缓存
        if (validateDate(shopInfo, redisCache)) {
            saveShopInfo2ReidsCache(shopInfo);
        }

        // 重建JVM缓存
        if (validateDate(shopInfo, jvmCache)) {
            saveShopInfo2LocalCache(shopInfo);
        }
    }
}
