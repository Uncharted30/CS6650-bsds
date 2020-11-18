package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConsumerProperties {

    private static final String PATH = "./consumer.properties";

    public static String MQ_HOST;
    public static String MQ_USERNAME;
    public static String MQ_PASSWORD;
    public static String QUEUE_NAME;
    public static String DB_URL;
    public static String DB_USERNAME;
    public static String DB_PASSWORD;

    static {

        try (FileInputStream file = new FileInputStream(PATH)) {
            Properties properties = new Properties();
            properties.load(file);
            MQ_HOST = properties.getProperty("MQ_HOST");
            MQ_USERNAME = properties.getProperty("MQ_USERNAME");
            MQ_PASSWORD = properties.getProperty("MQ_PASSWORD");
            QUEUE_NAME = properties.getProperty("QUEUE_NAME");
            DB_URL = properties.getProperty("DB_URL");
            DB_USERNAME = properties.getProperty("DB_USERNAME");
            DB_PASSWORD = properties.getProperty("DB_PASSWORD");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
