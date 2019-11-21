package com.yzchnb.productidextractor;

import com.yzchnb.productidextractor.ProductIdUtils.ProductIdManager;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class WebController {

    @Resource
    private ProductIdManager productIdManager;

    @RequestMapping("/test")
    public String test(){
        return "connected";
    }

    @RequestMapping(value = "/getProductId", method = RequestMethod.GET)
    public String getProductId(){
        return productIdManager.getProductId();
    }

    @RequestMapping(value = "/report/{productId}/{status}", method = RequestMethod.GET)
    public void report(@PathVariable String productId, @PathVariable String status){
        System.out.println("收到爬取报告：爬取 " + productId + " " + status);
        if(status.equals("success")){
            productIdManager.successCrawled(productId);
        }else{
            productIdManager.failCrawled(productId);
        }
    }

    @PostMapping("/initFinished")
    public void initFinished(@RequestBody String productIds){
        System.out.println("收到爬虫服务初始化信息。");
        System.out.println("已经完成的爬虫有：" + productIds);
        productIdManager.removeFinished(productIds.split(","));
    }


}
