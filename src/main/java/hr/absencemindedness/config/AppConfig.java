package hr.absencemindedness.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {

    private static final Properties props = new Properties();

    static{
        try(InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")){
            if (in == null){
                throw new IllegalStateException("application.properties not found");
            }
            props.load(in);
        } catch(IOException e){
            throw new IllegalStateException("Failed to load application.properties", e);
        }
    }

    private AppConfig(){
        throw new IllegalStateException("Utility class");
    }

    public static String get(String key){
        return props.getProperty(key);
    }

    public static String get(String key, String defaultValue){
        return props.getProperty(key, defaultValue);
    }
}
