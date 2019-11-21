package com.yzchnb.productidextractor;

import com.yzchnb.productidextractor.ProductIdUtils.ProductIdManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class ProductIdExtractorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductIdExtractorApplication.class, args);
        for (String arg : args) {
            System.out.println(arg);
        }
        try{
            System.out.println("使用参数路径：" + args[0]);
            SettingsManager.setCsvFilePath(args[0]);
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("使用默认路径");
        }
        ProductIdManager.initProductIds();

    }

}
