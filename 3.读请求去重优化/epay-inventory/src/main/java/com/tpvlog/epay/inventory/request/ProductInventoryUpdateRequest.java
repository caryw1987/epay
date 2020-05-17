package com.tpvlog.epay.inventory.request;

import com.tpvlog.epay.inventory.entity.ProductInventory;
import com.tpvlog.epay.inventory.service.ProductInventoryService;

/**
 * 商户库存更新请求：
 * <p>
 * 1.删除缓存
 * 2.更新数据库
 */
public class ProductInventoryUpdateRequest implements Request {

    /**
     * 商品信息
     */
    private ProductInventory productInventory;

    /**
     * 商品库存服务
     */
    private ProductInventoryService productInventoryService;

    public ProductInventoryUpdateRequest(ProductInventory productInventory, ProductInventoryService productInventoryService) {
        this.productInventory = productInventory;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {
        // 1.删除缓存
        boolean result = productInventoryService.removeCachedProductInventory(productInventory);

        // 2.更新数据库
        if (result) {
            productInventoryService.updateProductInventory(productInventory);
        }
    }

    @Override
    public Long getProductId() {
        return this.getProductId();
    }

    @Override
    public Boolean isForceFresh() {
        return false;
    }
}
