package com.goeuro.techtest;

import java.io.IOException;
import java.util.Properties;
import javax.ws.rs.WebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author achir
 */
public class TechTest {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: java -jar GoEuroTest.jar <city> <outputFile>");
            return;
        }

        Logger log = LoggerFactory.getLogger(TechTest.class);
        log.info("Initializing API test");

        String city = args[0];
        String outputFilename = args[1];

        Properties properties;
        Config props = new Config();

        try {
            properties = props.getProperties();
        } catch (IOException ex) {
            log.error("Configuration error", ex);
            return;
        }

        log.debug("API endpoint: {}", properties.getProperty("endpoint"));
        log.debug("Searching city: {}", city);
        
        LocationQuery query = new LocationQuery();
        String results;
        try {
            results = query.request(properties.getProperty("endpoint"), city);
        } catch (WebApplicationException e) {
            log.error("Cannot get results from remote server", e);
            return;
        }

        log.debug("String results {}", results);
        
        JSONToCSVConverter jsonToCSV = new JSONToCSVConverter();
        try {
            log.debug("Outputting to file: {}", outputFilename);
            jsonToCSV.convertToFile(results, outputFilename);
        } catch (IOException e) {
            log.error("Failed to write to output file", e);
            return;
        }

        log.info("Terminating API test");
    }
}
