package com.yzchnb.moviecrawler;

import com.yzchnb.moviecrawler.CrawlerUtils.Crawler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Component
public class SettingsManager {
    private static String htmlBaseDirPath;
    private static String tempCaptchaDirPath;
    private static int crawlerThreadsNum;
    private static boolean initOver = false;
    private static boolean useRecognition = false;
    static {
        try {
            Properties props = new Properties();
            props.load(Crawler.class.getClassLoader().getResourceAsStream("settings.properties"));
            String baseDirPath = props.getProperty("baseDir");
            crawlerThreadsNum = Integer.parseInt(props.getProperty("CrawlerThreadsNum"));
            File htmlBaseDir = new File(baseDirPath, "htmls");
            File tempCaptchaDir = new File(baseDirPath, "captchas");
            File baseDir = new File(baseDirPath);
            if(!baseDir.exists()){
                System.out.println("默认文件夹不存在！需要使用命令行参数获得文件夹路径。");
            }else{
                if (!htmlBaseDir.exists()) {
                    if (!htmlBaseDir.mkdir()) {
                        throw new IOException("htmls 文件夹创建失败！");
                    }
                }
                if(!tempCaptchaDir.exists()){
                    if(!tempCaptchaDir.mkdir()){
                        throw new IOException("tempCaptcha文件夹创建失败！");
                    }
                }
                htmlBaseDirPath = htmlBaseDir.getAbsolutePath();
                tempCaptchaDirPath = tempCaptchaDir.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getHtmlBaseDirPath() {
        return htmlBaseDirPath;
    }

    public static String getTempCaptchaDirPath() {
        return tempCaptchaDirPath;
    }

    public static int getCrawlerThreadsNum() {
        return crawlerThreadsNum;
    }

    public static void setCrawlerThreadsNum(int i){
        crawlerThreadsNum = i;
    }

    public static void setInitOver(boolean b){
        initOver = b;
    }

    public static boolean getInitOver(){
        return initOver;
    }

    public static void setBaseDirPath(String path){
        try {
            File htmlBaseDir = new File(path, "htmls");
            File tempCaptchaDir = new File(path, "captchas");
            File baseDir = new File(path);
            if(!baseDir.exists()){
                System.out.println("参数路径不存在！");
                throw new IOException("参数路径不存在！");
            }else{
                if (!htmlBaseDir.exists()) {
                    if (!htmlBaseDir.mkdir()) {
                        throw new IOException("htmls 文件夹创建失败！");
                    }
                }
                if(!tempCaptchaDir.exists()){
                    if(!tempCaptchaDir.mkdir()){
                        throw new IOException("tempCaptcha文件夹创建失败！");
                    }
                }
                htmlBaseDirPath = htmlBaseDir.getAbsolutePath();
                tempCaptchaDirPath = tempCaptchaDir.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void setUseRecognition(boolean use){
        useRecognition = use;
    }

    public static boolean isUseRecognition(){
        return useRecognition;
    }
}
