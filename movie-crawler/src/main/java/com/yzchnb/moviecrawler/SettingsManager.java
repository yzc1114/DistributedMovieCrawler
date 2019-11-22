package com.yzchnb.moviecrawler;

import com.yzchnb.moviecrawler.CrawlerUtils.Crawler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Component
public class SettingsManager {
    private static String baseDirPath;
    private static String htmlBaseDirPath;
    private static int crawlerThreadsNum;
    private static boolean initOver = false;
    static {
        try {
            Properties props = new Properties();
            props.load(Crawler.class.getClassLoader().getResourceAsStream("settings.properties"));
            baseDirPath = props.getProperty("baseDir");
            crawlerThreadsNum = Integer.parseInt(props.getProperty("CrawlerThreadsNum"));
            htmlBaseDirPath = baseDirPath + "htmls";
            File htmlBaseDir = new File(htmlBaseDirPath);
            File baseDir = new File(baseDirPath);
            if(!baseDir.exists()){
                System.out.println("默认文件夹不存在！需要使用命令行参数获得文件夹路径。");
            }else{
                if (!htmlBaseDir.exists()) {
                    if (!htmlBaseDir.mkdir()) {
                        throw new IOException("htmls 文件夹创建失败！");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getHtmlBaseDirPath() {
        return htmlBaseDirPath;
    }

    public static int getCrawlerThreadsNum() {
        return crawlerThreadsNum;
    }

    public static void setInitOver(boolean b){
        initOver = b;
    }

    public static boolean getInitOver(){
        return initOver;
    }

    public static void setBaseDirPath(String path){
        try{
            File newPath = new File(path);
            if(newPath.isFile() || !newPath.exists()){
                System.out.println("需要合法的文件夹路径！");
                throw new IOException("需要合法的文件夹路径！");
            }
            File htmlBaseDir = new File(newPath, "htmls");
            if(!htmlBaseDir.exists()){
                htmlBaseDir.mkdir();
            }
            htmlBaseDirPath = htmlBaseDir.getAbsolutePath();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("创建htmls文件夹失败！");
            System.exit(-1);
        }
    }
}
