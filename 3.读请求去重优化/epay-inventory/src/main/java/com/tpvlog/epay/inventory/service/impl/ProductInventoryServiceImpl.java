package com.tpvlog.epay.inventory.service.impl;

import com.tpvlog.epay.inventory.dao.RedisDao;
import com.tpvlog.epay.inventory.entity.ProductInventory;
import com.tpvlog.epay.inventory.mapper.ProductInventoryMapper;
import com.tpvlog.epay.inventory.service.ProductInventoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("productInventoryService")
public class ProductInventoryServiceImpl implements ProductInventoryService {

    private static final Logger log = LoggerFactory.getLogger(ProductInventoryServiceImpl.class);

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private ProductInventoryMapper productInventoryMapper;

    @Override
    public ProductInventory queryProductInventory(Long productId) {
        ProductInventory result = productInventoryMapper.queryProductInventory(productId);
        if (result == null || result.getId() == null) {
            return null;
        } else {
            return result;
        }
    }

    @Override
    public ProductInventory queryCachedProductInventory(Long productId) {
        String key = "product:inventory:" + productId;
        String result = redisDao.get(key);
        if (StringUtils.isBlank(result)) {
            return null;
        }
        Long inventoryCnt = -1L;
        try {
            inventoryCnt = Long.valueOf(result);
        } catch (Exception ex) {
            log.error("库存数据格式错误:{}", result, ex);
            return null;
        }
        return new ProductInventory(productId, inventoryCnt);
    }

    @Override
    public Boolean setCachedProductInventory(ProductInventory productInventory) {
        try {
            String key = "product:inventory:" + productInventory.getProductId();
            redisDao.set(key, productInventory.getInventoryCnt());
            return true;
        } catch (Exception ex) {
            log.error("设置商品库存缓存失败:{}", productInventory, ex);
        }
        return false;
    }

    @Override
    public Boolean removeCachedProductInventory(ProductInventory productInventory) {
        try {
            String key = "product:inventory:" + productInventory.getProductId();
            redisDao.del(key);
            return true;
        } catch (Exception ex) {
            log.error("删除商品库存缓存失败:{}", productInventory, ex);
        }
        return false;
    }

    @Override
    public int updateProductInventory(ProductInventory productInventory) {
        return productInventoryMapper.updateProductInventory(productInventory);
    }
}
