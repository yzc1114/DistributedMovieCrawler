package com.yzchnb.moviecrawler.Communicators;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "product-id-extractor")
public interface ResultResponse {
    @RequestMapping(value = "/report/{productId}/{status}",method = RequestMethod.GET)
    String sendResponse(@PathVariable String productId, @PathVariable String status);
}
