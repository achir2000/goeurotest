package com.goeuro.techtest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author achir
 */
public class Config {

    public Properties getProperties() throws IOException {
        Properties prop = new Properties();
        String propFileName = "config.properties";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("Property file '" + propFileName + "' not found");
        }

        if (!prop.containsKey("endpoint")) {
            throw new IOException("Property 'endpoint' not found");
        }
        
        return prop;
    }

}
