package com.yzchnb.productidextractor.ProductIdUtils;

import com.yzchnb.productidextractor.SettingsManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class ProductIdManager {

    private ConcurrentLinkedQueue<ProductId> idQueue = new ConcurrentLinkedQueue<>();

    private final HashMap<String, ProductId> crawlingProducts = new HashMap<>();

    @PostConstruct
    public void initProductIds(){
        idQueue.clear();
        String productIdsCsvFilePath = SettingsManager.getCsvFilePath();
        File productIdsCsvFile = new File(productIdsCsvFilePath);
        try{
            if(!productIdsCsvFile.exists()){
                throw new IOException("productIds.csv 不存在！！！！！！！");
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(productIdsCsvFile));
            bufferedReader.readLine();
            while(true){
                String productId = bufferedReader.readLine();
                if(productId == null){
                    break;
                }
                idQueue.add(new ProductId(productId));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getProductId(){
        ProductId productId = idQueue.poll();
        if(productId == null){
            return null;
        }
        synchronized (crawlingProducts){
            crawlingProducts.put(productId.getId(), productId);
        }
        return productId.getId();
    }

    public void successCrawled(String productId){
        crawlingProducts.remove(productId);
    }

    public void removeFinished(String[] productIds){
        synchronized (idQueue){
            System.out.println("remove前，队列中的id数量：" + idQueue.size());
            HashSet<ProductId> currIds = new HashSet<>(idQueue);
            HashSet<ProductId> finishedIds = new HashSet<>();
            for (String productId : productIds) {
                finishedIds.add(new ProductId(productId));
            }
            currIds.removeAll(finishedIds);
            idQueue.clear();
            idQueue.addAll(currIds);
            System.out.println("remove后，队列中的id数量：" + idQueue.size());
        }
    }

    public void failCrawled(String productId){
        ProductId failedProductId = crawlingProducts.remove(productId);
        if(failedProductId.decreaseAndGetRemainedFailedTime() != 0){
            idQueue.add(failedProductId);
        }else{
            System.out.println("Giving up on " + productId);
        }
    }

    @Scheduled(fixedRate = 60 * 1000)
    private void removeNotRespondingCrawling(){
        synchronized (crawlingProducts){
            for (String s : crawlingProducts.keySet()) {
                if(System.currentTimeMillis() - crawlingProducts.get(s).getCreateTime() >= 600 * 1000){
                    crawlingProducts.remove(s);
                }
            }
        }
    }
}
