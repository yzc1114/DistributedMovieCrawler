package com.yzchnb.moviecrawler.Communicators;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "product-id-extractor")
public interface InitFinshed {
    @PostMapping("/initFinished")
    void initFinished(@RequestBody String productIds);
}
