package com.tpvlog.epay.cache.enums;

public enum LockType {
    PRODUCT("product", 0),
    SHOP("shop", 1);

    private String key;
    private Integer value;

    LockType(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "LockType{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}
