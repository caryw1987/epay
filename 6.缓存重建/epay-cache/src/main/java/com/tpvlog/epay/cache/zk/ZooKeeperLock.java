package com.tpvlog.epay.cache.zk;

import java.util.concurrent.CountDownLatch;

import com.tpvlog.epay.cache.controller.CacheController;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ZooKeeperLock,
 * <p>
 * 通过在 /lock 根路径下竞争创建临时节点来模拟排它锁。以下情况锁会被释放：
 * <p>
 * 1.当前获取锁的客户端发生宕机，那么ZooKeeper服务器上保存的临时性节点就会被删除；<br>
 * 2.当前获取锁的客户端正常执行完业务逻辑，客户端主动来将自己创建的临时节点删除。
 *
 * @author ressmix
 */
public class ZooKeeperLock implements InitializingBean {

    private final static String ROOT_PATH_LOCK = "lock";

    private static final Logger LOG = LoggerFactory.getLogger(ZooKeeperLock.class);

    @Autowired
    private CuratorFramework curatorFramework;

    /**
     * 获取分布式锁
     */
    public void acquire(String key) {
        String keyPath = "/" + ROOT_PATH_LOCK + "/" + key;
        while (true) {
            try {
                curatorFramework
                        .create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(keyPath);
                LOG.info("success to acquire lock for path:{}", keyPath);
                break;
            } catch (Exception e) {
                LOG.info("failed to acquire lock for path:{}", keyPath);
                LOG.info("while try again .......");
                try {
                    if (countDownLatch.getCount() <= 0) {
                        countDownLatch = new CountDownLatch(1);
                    }
                    countDownLatch.await();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 释放分布式锁
     */
    public boolean release(String key) {
        try {
            String keyPath = "/" + ROOT_PATH_LOCK + "/" + key;
            if (curatorFramework.checkExists().forPath(keyPath) != null) {
                curatorFramework.delete().forPath(keyPath);
            }
        } catch (Exception e) {
            LOG.error("failed to release lock");
            return false;
        }
        return true;
    }

    /**
     * 创建 watcher 事件
     */
    private void addWatcher(String path) throws Exception {
        String keyPath;
        if (path.equals(ROOT_PATH_LOCK)) {
            keyPath = "/" + path;
        } else {
            keyPath = "/" + ROOT_PATH_LOCK + "/" + path;
        }
        final PathChildrenCache cache = new PathChildrenCache(curatorFramework, keyPath, false);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener((client, event) -> {
            if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                String oldPath = event.getData().getPath();
                LOG.info("上一个节点 " + oldPath + " 已经被断开");
                if (oldPath.contains(path)) {
                    //释放计数器，让当前的请求获取锁
                    countDownLatch.countDown();
                }
            }
        });
    }

    /**
     * 创建父节点，作为永久节点
     */
    @Override
    public void afterPropertiesSet() {
        curatorFramework = curatorFramework.usingNamespace("lock-namespace");
        String path = "/" + ROOT_PATH_LOCK;
        try {
            if (curatorFramework.checkExists().forPath(path) == null) {
                curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath(path);
            }
            addWatcher(ROOT_PATH_LOCK);
            LOG.info("root path 的 watcher 事件创建成功");
        } catch (Exception e) {
            LOG.error("connect zookeeper fail，please check the log", e);
        }
    }
}
