package de.foursoft.discordbot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);
    private final Properties properties;
    
    public Configuration(String pathToConfig) {
        properties = new Properties();
        File file = new File(pathToConfig);
        String absolutePath = file.getAbsolutePath();
        if (!file.isFile())  {
            LOGGER.info("Given file does not exist, trying to create it..");

            String content = "token=<here token>";
            try {
                Files.write(Paths.get(pathToConfig),
                            content.getBytes(StandardCharsets.UTF_8));

                LOGGER.info("Wrote initial contents to file {}.", absolutePath);
            } catch (IOException e) {
                LOGGER.warn("Failed to write contents to file {}.", absolutePath);
            }
            System.exit(-1);
        }

        InputStream resourceStream;
        try {
            resourceStream = new FileInputStream(file);
            properties.load(resourceStream);
        } catch (FileNotFoundException e) {
            LOGGER.error("Couldn't get configuration file {}", absolutePath);
            System.exit(-1);
        } catch (IOException e) {
            LOGGER.error("Couldn't load configuration file {}", absolutePath, e);
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