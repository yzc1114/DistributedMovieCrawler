package com.yzchnb.moviecrawler.CrawlerUtils;

import com.yzchnb.moviecrawler.Communicators.InitFinshed;
import com.yzchnb.moviecrawler.Communicators.ProductIdGetter;
import com.yzchnb.moviecrawler.Communicators.ResultResponse;
import com.yzchnb.moviecrawler.SettingsManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.Set;

@Component
@Configuration
@EnableScheduling
public class CrawlerScheduler {

    @Resource
    private Crawler crawler;

    @Resource
    private ProductIdGetter productIdGetter;

    @Resource
    private ResultResponse resultResponse;

    @Resource
    private InitFinshed initFinshed;

    private static int maxCrawlingNum = SettingsManager.getCrawlerThreadsNum();

    private int currCrawlingNum = 0;


    private boolean initFinished = false;
    private static boolean entered = false;

    private void initFinished(){
        if(entered){
            return;
        }
        entered = true;
        //Tell the productIdExtractor that some productIds had been crawled
        if(!SettingsManager.getInitOver()){
            System.out.println("未初始化完毕就开始发送信息。");
            System.exit(-1);
        }
        String htmlBaseDirPath = SettingsManager.getHtmlBaseDirPath();
        File htmlsDir = new File(htmlBaseDirPath);
        if(!htmlsDir.exists()){
            System.out.println("htmls文件夹不存在！");
            htmlsDir.mkdir();
            initFinished = true;
            return;
        }
        String[] listed = htmlsDir.list((f, s) ->
                s.split("\\.").length == 2
        );
        if(listed == null || listed.length == 0){
            System.out.println("htmls文件夹为空。");
            initFinished = true;
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (String s : listed) {
            String productId = s.split("\\.")[0];
            if(productId.length() > 0){
                builder.append(productId);
                builder.append(',');
            }
            if(builder.length() >= 1000){
                builder.deleteCharAt(builder.length() - 1);
                String finishedProductIds = builder.toString();
                System.out.println(finishedProductIds);
                initFinshed.initFinished(finishedProductIds);
                builder = new StringBuilder();
            }
        }
        System.out.println("向服务器声明已爬取完毕的id完毕。");
        initFinished = true;
    }

    @Scheduled(fixedRate = 1500)
    @Async("scheduledPoolTaskExecutor")
    public void doCrawl() {
        boolean told = false;
        if(!SettingsManager.getInitOver()){
            return;
        }
        if(!initFinished){
            this.initFinished();
            return;
        }
        if(currCrawlingNum >= maxCrawlingNum){
            return;
        }
        String productId = productIdGetter.getProductId();
        if(productId == null){
            return;
        }
        try{
            System.out.println("准备爬取：" + productId);
            currCrawlingNum++;
            if(crawler.crawlOneProduct(productId)){
                resultResponse.sendResponse(productId, "success");
                told = true;
            }else{
                resultResponse.sendResponse(productId, "fail");
                told = true;
            }
            currCrawlingNum--;
        }finally {
            if(!told){
                resultResponse.sendResponse(productId, "fail");
            }
        }
    }
}