package com.yzchnb.moviecrawler.Communicators;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "product-id-extractor", url = "${feign.url}")
public interface ProductIdGetter {
    @RequestMapping(value = "/getProductId",method = RequestMethod.GET)
    String getProductId();
}