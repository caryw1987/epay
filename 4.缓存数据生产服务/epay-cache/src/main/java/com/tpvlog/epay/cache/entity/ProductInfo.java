package com.tpvlog.epay.cache.entity;

import java.io.Serializable;

/**
 * 商品信息
 *
 * @author ressmix
 */
public class ProductInfo implements Serializable {
    private static final long serialVersionUID = -2580172219285459559L;
    private Long id;
    private String name;
    private Long price;

    public ProductInfo() {

    }

    public ProductInfo(Long id, String name, Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}

