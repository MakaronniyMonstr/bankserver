package com.vesko.configuration;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;

public class PropertiesReader {
    private static final Yaml yaml = new Yaml();

    public static Properties loadProperties(String path) throws IOException {
        try (var resourceStream = PropertiesReader.class.getClassLoader().getResourceAsStream(path)) {
            return yaml.loadAs(resourceStream, Properties.class);
        }
    }
}
