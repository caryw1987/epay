package com.tpvlog.epay.inventory.service;

import com.tpvlog.epay.inventory.entity.ProductInventory;

/**
 * 商品库存服务
 *
 * @author ressmix
 */
public interface ProductInventoryService {

    /**
     * 根据商品id查询商品库存
     *
     * @param productId 商品id
     * @return 商品库存
     */
    ProductInventory queryProductInventory(Long productId);

    /**
     * 根据商品id，从缓存查询商品库存
     *
     * @param productId 商品id
     * @return 商品库存
     */
    ProductInventory queryCachedProductInventory(Long productId);

    /**
     * 设置缓存中的商品库存
     *
     * @param productInventory 商品库存
     */
    Boolean setCachedProductInventory(ProductInventory productInventory);

    /**
     * 删除商品库存缓存
     *
     * @param productInventory 商品库存
     */
    Boolean removeCachedProductInventory(ProductInventory productInventory);

    /**
     * 更新数据库中的商品库存
     *
     * @param productInventory 商品库存
     */
    int updateProductInventory(ProductInventory productInventory);
}
