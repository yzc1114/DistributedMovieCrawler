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
                int numOfThreads = Integer.parseUnsignedInt(args[1]);
                if(numOfThreads == 0){
                    throw new NumberFormatException();
                }
            }
            if(args.length >= 3){
                SettingsManager.setUseRecognition(true);
            }
        }catch (NumberFormatException e){
            System.out.println("输入线程数的格式不对！");
            System.exit(-1);
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("使用默认路径");
        }
        SettingsManager.setInitOver(true);
    }

}
