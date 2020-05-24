package com.tpvlog.epay.inventory.entity;

/**
 * 商品库存Bean
 */
public class ProductInventory {

    private Long id;

    /**
     * 商品id
     */
    private Long productId;
    /**
     * 库存数量
     */
    private Long inventoryCnt;

    public ProductInventory() {

    }

    public ProductInventory(Long productId, Long inventoryCnt) {
        this.productId = productId;
        this.inventoryCnt = inventoryCnt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getInventoryCnt() {
        return inventoryCnt;
    }

    public void setInventoryCnt(Long inventoryCnt) {
        this.inventoryCnt = inventoryCnt;
    }

    @Override
    public String toString() {
        return "ProductInventory{" +
                "id=" + id +
                ", productId=" + productId +
                ", inventoryCnt=" + inventoryCnt +
                '}';
    }
}
