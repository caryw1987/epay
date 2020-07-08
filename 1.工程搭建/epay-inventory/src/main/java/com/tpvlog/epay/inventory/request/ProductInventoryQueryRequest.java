package com.tpvlog.epay.inventory.request;

import com.tpvlog.epay.inventory.entity.ProductInventory;
import com.tpvlog.epay.inventory.service.ProductInventoryService;

/**
 * 商户库存查询请求：
 * <p>
 * 1.从数据库查询商品库存
 * 2.更新缓存
 */
public class ProductInventoryQueryRequest implements Request {

    /**
     * 商品信息
     */
    private ProductInventory productInventory;

    /**
     * 商品库存服务
     */
    private ProductInventoryService productInventoryService;


    public ProductInventoryQueryRequest(ProductInventory productInventory, ProductInventoryService productInventoryService) {
        this.productInventory = productInventory;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {
        // 1.从数据库查询
        ProductInventory productFromDB = productInventoryService.queryProductInventory(productInventory.getProductId());
        if (productFromDB == null) {
            return;
        }
        // 2.更新缓存
        productInventoryService.setCachedProductInventory(productFromDB);
    }

    @Override
    public Long getProductId() {
        return this.productInventory.getProductId();
    }
}
