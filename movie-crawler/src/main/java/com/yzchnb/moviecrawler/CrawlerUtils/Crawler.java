package com.yzchnb.moviecrawler.CrawlerUtils;

import com.yzchnb.moviecrawler.SettingsManager;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class Crawler {

    private static String[] userAgents = {"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36 OPR/37.0.2178.32",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586",
            "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
            "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0)",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 BIDUBrowser/8.3 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.277.400 QQBrowser/9.4.7658.400",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 UBrowser/5.6.12150.8 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 TheWorld 7",
            "Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/60.0"};

    private Map<String, String> cookies = new HashMap<>();

    private Random random = new Random();

    public boolean crawlOneProduct(String productId){
        String baseUrl = "https://www.amazon.com/gp/product/";
        try{
            Connection.Response response;
                response = Jsoup.connect(baseUrl + productId).timeout(30000)
                        .method(Connection.Method.GET)
                        .cookies(cookies)
                        .header("user-agent", userAgents[random.nextInt(userAgents.length)])
                        .execute();

            cookies = response.cookies();

            Document doc = response.parse();
            if(!checkValidity(doc)){
                System.out.println("爬取 " + productId + " 失败，原因：检测到机器人");
                return false;
            }
            return saveDoc(doc, productId);
        }catch (IOException e){
            System.out.println("爬取 " + productId + " 失败，原因：连接失败");
            //e.printStackTrace();
            return false;
        }
    }

    private boolean checkValidity(Document doc){
        Elements lisOld = doc.select("#detail-bullets > table > tbody > tr > td > div > ul > li");
        Elements lisNew = doc.select("#a-page > div.av-page-desktop.avu-retail-page > div.avu-content.avu-section > div > div > div.DVWebNode-detail-atf-wrapper.DVWebNode > div.av-detail-section > div > div._2vWb4y.dv-dp-node-meta-info > div > div");
        return lisOld.size() != 0 || lisNew.size() != 0;
    }

    private boolean saveDoc(Document doc, String productId){
        System.out.println("爬取：" + productId + " 成功");
        String htmlBaseDirPath = SettingsManager.getHtmlBaseDirPath();
        File htmlBaseDir = new File(htmlBaseDirPath);
        if(!htmlBaseDir.exists()){
            htmlBaseDir.mkdir();
        }
        try{
            File htmlFile = new File(htmlBaseDirPath + "/" + productId + ".html");
            System.out.println("存入文件：" + htmlFile.getAbsolutePath());
            if(htmlFile.exists()){
                return true;
            }
            if(!htmlFile.createNewFile()){
                return false;
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(htmlFile));
            bufferedWriter.write(doc.html());
            bufferedWriter.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
