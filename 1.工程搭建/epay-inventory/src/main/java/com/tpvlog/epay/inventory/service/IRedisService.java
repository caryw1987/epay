package com.tpvlog.epay.inventory.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author ressmix
 * @version 1.0
 * @date 2018/4/16 16:26
 * @since 1.0
 */
public interface IRedisService {
    <T> void set(String key, T value);

    <T> void set(String key, T value, long expire, TimeUnit timeUnit);

    <T> T get(String key);

    boolean expire(String key, long expire);

    void del(String key);

    void delBatch(Set<String> keys);

    /**
     * 线上请不要使用这种方法，会造成全局扫描，数据量大时会引起卡顿
     *
     * @param keyPrefix key的前缀
     */
    @Deprecated
    void delBatch(String keyPrefix);

    <T> void setList(String key, List<T> list);

    <T> void setList(String key, List<T> list, long expire, TimeUnit timeUnit);

    <T> List<T> getList(String key, Class<T> clz);

    boolean hasKey(String key);

    long getExpire(String key);

    Set<String> keySet(String keyPrefix);
}
