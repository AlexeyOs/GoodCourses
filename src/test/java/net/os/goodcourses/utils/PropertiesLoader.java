package net.os.goodcourses.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {
    public static Properties loadProperties(String fileName) throws IOException {
        Properties configuration = new Properties();
        configuration.load(new FileReader(fileName));
        return configuration;
    }
}
