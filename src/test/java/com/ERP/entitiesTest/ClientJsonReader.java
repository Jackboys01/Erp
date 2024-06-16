package com.ERP.entitiesTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ClientJsonReader {

    private static final Logger logger = LoggerFactory.getLogger(ClientJsonReader.class);

    public Map<String, Object> readFile(String path) throws IOException {
        InputStream inputStream = null;
        try {
            // Load the JSON file from the resources folder
            inputStream = ClientJsonReader.class.getClassLoader().getResourceAsStream("payloads/" + path + ".json");
            if (inputStream == null) {
                logger.error("File not found: payloads/{}.json", path);
                throw new IllegalArgumentException("File not found: payloads/" + path + ".json");
            }

            // Create an ObjectMapper to parse the JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(inputStream);

            // Convert JSON data to a Map
            return objectMapper.convertValue(rootNode, Map.class);
        } catch (IOException e) {
            logger.error("Error reading file: payloads/{}.json", path, e);
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("Error closing input stream for file: payloads/{}.json", path, e);
                }
            }
        }
    }
}
