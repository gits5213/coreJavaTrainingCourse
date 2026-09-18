package com.company.automation.config;

import com.company.automation.driver.BrowserType;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public final class Config {

    private final Properties properties;

    Config(Properties properties) {
        this.properties = properties;
    }

    public static Config load(String classpathResource) {
        Properties properties = new Properties();
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream(classpathResource)) {
            if (in == null) {
                throw new IllegalArgumentException("Missing classpath resource: " + classpathResource);
            }
            properties.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not load " + classpathResource, e);
        }
        return new Config(properties);
    }

    public static Config qa() {
        return load("config/qa.properties");
    }

    public BrowserType browser() {
        String override = System.getProperty("browser");
        String value = override != null ? override : properties.getProperty("browser", "chrome");
        return BrowserType.from(value);
    }

    public String baseUrl() {
        return properties.getProperty("baseUrl");
    }
}
