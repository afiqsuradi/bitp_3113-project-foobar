package edu.foobar.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static Properties prop;
    public static Properties getConfig(){
        if(prop != null){ return prop; }
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/application.properties");
            props.load(fis);
            prop = props;
        }catch(IOException e){
            e.printStackTrace();
        }
        return prop;
    }
}
