package com.goeuro.techtest;

import com.csvreader.CsvWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author achir
 */
public class JSONToCSVConverter {

    private static final Logger log = LoggerFactory.getLogger(TechTest.class);

    public void convertToFile(String jsonString, String filename) throws IOException {
        List<Map<String, String>> entries = parseJSON(jsonString);
        writeToCSV(filename, entries);
    }

    private List parseJSON(String jsonString) {
        log.debug("Parsing json string {}", jsonString);
        JSONArray results = new JSONArray(jsonString);

        ArrayList<Map<String, String>> entries = new ArrayList<>();

        for (int counter = 0; counter < results.length(); ++counter) {
            Map<String, String> newEntry = new HashMap<>();

            JSONObject entry = results.getJSONObject(counter);
            log.debug("Processing entry {}", entry);
            
            newEntry.put("_id", entry.get("_id").toString());            
            newEntry.put("name", entry.get("name").toString());
            newEntry.put("type", entry.get("type").toString());

            JSONObject geoPosition = entry.getJSONObject("geo_position");
            if (geoPosition == null) {
                log.warn("No geoPosition element found - writing blanks");
                newEntry.put("latitude", "");
                newEntry.put("longitude", "");
            } else {
                newEntry.put("latitude", geoPosition.get("latitude").toString());
                newEntry.put("longitude", geoPosition.get("longitude").toString());
            }

            entries.add(newEntry);
        }

        log.debug("Successfully parsed {} JSON entities from input string", entries.size());
        return entries;
    }

    private void writeToCSV(String filename, List<Map<String, String>> entries) throws FileAlreadyExistsException, IOException {
        // ensure that the file does not already exist
        boolean alreadyExists = new File(filename).exists();
        if (alreadyExists) {
            throw new FileAlreadyExistsException(filename);
        }

        CsvWriter csvOutput = new CsvWriter(new FileWriter(filename), ',');

        if (!alreadyExists) {
            log.debug("Writing header to file");
            csvOutput.write("_id");
            csvOutput.write("name");
            csvOutput.write("type");
            csvOutput.write("latitude");
            csvOutput.write("longitude");
            csvOutput.endRecord();
        }

        for (Map<String, String> currentEntry : entries) {
            String entryId = currentEntry.get("_id");
            log.debug("Writing entry {}", entryId);
            csvOutput.write(entryId);
            csvOutput.write(currentEntry.get("name"));
            csvOutput.write(currentEntry.get("type"));
            csvOutput.write(currentEntry.get("latitude"));
            csvOutput.write(currentEntry.get("longitude"));
            csvOutput.endRecord();
        }

        log.debug("CSV write operation completed - closing file");
        csvOutput.close();
    }
}
