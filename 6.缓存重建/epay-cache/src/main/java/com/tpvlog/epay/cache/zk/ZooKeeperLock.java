package com.tpvlog.epay.cache.zk;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.tpvlog.epay.cache.controller.CacheController;
import com.tpvlog.epay.cache.enums.LockType;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ZooKeeperLock
 * <p>
 * 通过在 /lock 根路径下竞争创建临时节点来模拟排它锁。以下情况锁会被释放：
 * <p>
 * 1.当前获取锁的客户端发生宕机，那么ZooKeeper服务器上保存的临时性节点就会被删除；<br>
 * 2.当前获取锁的客户端正常执行完业务逻辑，客户端主动来将自己创建的临时节点删除。
 *
 * @author ressmix
 */
@Service
public class ZooKeeperLock implements InitializingBean {

    private final static String ROOT_PATH_LOCK = "distributed_lock";

    private static final Logger LOG = LoggerFactory.getLogger(ZooKeeperLock.class);

    private ConcurrentHashMap<String, InterProcessMutex> lockMap = new ConcurrentHashMap<>();

    @Autowired
    private CuratorFramework curatorFramework;

    /**
     * 获取锁
     *
     * @param type
     * @param key
     * @return
     */
    public InterProcessMutex getMutex(LockType type, String key) {
        String keyPath = "/" + ROOT_PATH_LOCK + "/" + type.getKey() + "/" + key;
        return new InterProcessMutex(curatorFramework, keyPath);
    }

    /**
     * 创建父节点，作为永久节点
     */
    @Override
    public void afterPropertiesSet() {
        String path = null;
        try {
            for (LockType type : LockType.values()) {
                String key = type.getKey();
                path = "/" + ROOT_PATH_LOCK + "/" + key;
                if (curatorFramework.checkExists().forPath(path) == null) {
                    LOG.info("创建分布式锁根节点：{}", path);
                    curatorFramework.create()
                            .creatingParentsIfNeeded()
                            .withMode(CreateMode.PERSISTENT)
                            .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                            .forPath(path);
                }
            }
        } catch (Exception e) {
            LOG.error("创建分布式锁根节点失败:{}", path, e);
        }
    }
}
