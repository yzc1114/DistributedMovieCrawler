package com.yzchnb.moviecrawler;

import com.yzchnb.moviecrawler.CrawlerUtils.CrawlerScheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class MovieCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieCrawlerApplication.class, args);
        try{
            System.out.println("使用参数路径：" + args[0]);
            SettingsManager.setBaseDirPath(args[0]);
            if(args.length >= 2){
                SettingsManager.setUseRecognition(true);
            }
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("使用默认路径");
        }
        SettingsManager.setInitOver(true);
    }

}
