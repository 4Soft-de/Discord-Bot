package de.foursoft.discordbot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);
    private final Properties properties;
    
    public Configuration(String pathToConfig) {
        properties = new Properties();
        InputStream resourceStream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(pathToConfig);

        if (resourceStream == null) {
            LOGGER.error("Couldn't get configuration file {}", pathToConfig);
            System.exit(-1);
        }

        try {
            properties.load(resourceStream);
        } catch (IOException e) {
            LOGGER.error("Couldn't load configuration file {}", pathToConfig, e);
            System.exit(-1);
        }
    }

    public String getToken() {
        return getValue("token");
    }
    
    public String getValue(String key)  {
        return properties.getProperty(key);
    }
    
}