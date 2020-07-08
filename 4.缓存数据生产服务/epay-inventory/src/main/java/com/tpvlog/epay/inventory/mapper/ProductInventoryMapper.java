package com.tpvlog.epay.inventory.mapper;

import com.tpvlog.epay.inventory.entity.ProductInventory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 商品库存Mapper
 * 在主类里面已经填写了MapperScan之后，这个注解{@link Repository} 是否添加已经无关紧要
 */
@Repository
public interface ProductInventoryMapper {

    /**
     * 更新商品库存
     *
     * @param productInventory 商品库存
     */
    int updateProductInventory(ProductInventory productInventory);

    /**
     * 查询商品库存
     *
     * @param productId 商品id
     * @return 商品库存信息
     */
    ProductInventory queryProductInventory(@Param("productId") Long productId);

}
