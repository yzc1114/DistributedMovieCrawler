package com.yzchnb.moviecrawler;

import com.yzchnb.moviecrawler.CrawlerUtils.Crawler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;

@RestController
public class WebController {

    @Resource
    private Crawler crawler;

    private static String htmlsBaseDirPath = SettingsManager.getHtmlBaseDirPath();


    @RequestMapping("/crawl/{productId}")
    @ResponseBody
    public String crawl(@PathVariable String productId){
        return crawler.crawlOneProduct(productId) ? "success" : "fail";
    }

    @RequestMapping("/getFinishedProducts")
    @ResponseBody
    public String getFinishedProducts(){
        File htmlDir = new File(htmlsBaseDirPath);
        if(!htmlDir.exists()){
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String s : htmlDir.list()) {
            String productId = s.split("\\.")[0];
            if(productId.length() == 0){
                continue;
            }
            builder.append(productId);
            builder.append(',');
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
