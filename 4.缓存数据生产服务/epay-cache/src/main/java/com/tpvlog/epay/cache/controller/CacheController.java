package com.tpvlog.epay.cache.controller;

import com.tpvlog.epay.cache.entity.ProductInfo;
import com.tpvlog.epay.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CacheController {
    @Autowired
    private CacheService cacheService;

    @RequestMapping("/testUpdateCache")
    @ResponseBody
    public String testUpdateCache(ProductInfo productInfo) {
        cacheService.updateLocalCache(productInfo);
        return "success";
    }

    @RequestMapping("/testQueryCache/{id}")
    @ResponseBody
    public ProductInfo testGetCache(@PathVariable("id") Long id) {
        return cacheService.queryLocalCache(id);
    }
}
