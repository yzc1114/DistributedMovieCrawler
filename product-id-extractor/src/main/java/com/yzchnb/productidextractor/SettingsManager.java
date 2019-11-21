package com.yzchnb.productidextractor;


import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Component
public class SettingsManager {
    private static String csvFilePath;
    static {
        try{
            Properties props = new Properties();
            props.load(SettingsManager.class.getClassLoader().getResourceAsStream("Settings.properties"));
            csvFilePath = props.getProperty("csvFilePath");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static String getCsvFilePath(){
        return csvFilePath;
    }

    public static void setCsvFilePath(String path){
        File csvFile = new File(path);
        if(!csvFile.exists()){
            System.out.println("该路径的文件不存在！");
            System.exit(-1);
        }
        csvFilePath = csvFile.getAbsolutePath();
    }

}
