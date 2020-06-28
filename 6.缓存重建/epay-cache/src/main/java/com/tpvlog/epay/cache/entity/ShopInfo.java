package com.tpvlog.epay.cache.entity;

/**
 * 店铺信息
 *
 * @author Administrator
 */
public class ShopInfo {

    private Long id;
    private Long shopId;
    private String name;
    private Integer level;
    private Double goodCommentRate;
    private String modifiedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Double getGoodCommentRate() {
        return goodCommentRate;
    }

    public void setGoodCommentRate(Double goodCommentRate) {
        this.goodCommentRate = goodCommentRate;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    @Override
    public String toString() {
        return "ShopInfo{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", goodCommentRate=" + goodCommentRate +
                ", modifiedTime='" + modifiedTime + '\'' +
                '}';
    }
}
