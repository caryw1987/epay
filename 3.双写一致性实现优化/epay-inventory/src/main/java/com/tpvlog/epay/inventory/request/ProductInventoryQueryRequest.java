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
     * 是否强制刷新缓存
     */
    private Boolean isForceRefresh = false;

    /**
     * 商品信息
     */
    private ProductInventory productInventory;

    /**
     * 商品库存服务
     */
    private ProductInventoryService productInventoryService;


    public ProductInventoryQueryRequest(ProductInventory productInventory, ProductInventoryService productInventoryService, Boolean forceRefresh) {
        this.productInventory = productInventory;
        this.productInventoryService = productInventoryService;
        this.isForceRefresh = forceRefresh;
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
        return this.getProductId();
    }

    @Override
    public Boolean isForceFresh() {
        return this.isForceRefresh;
    }
}
