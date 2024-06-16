package com.ERP.entitiesTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class JsonReader {

public Map<String, Object> readFile(String path) throws IOException {
    // Load the JSON file from the resources folder
    InputStream inputStream = JsonReader.class.getClassLoader().getResourceAsStream("payloads/" + path + ".json");

    // Create an ObjectMapper to parse the JSON
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode = objectMapper.readTree(inputStream);

    // Convert JSON data to a Map
    Map<String, Object> dataMap = objectMapper.convertValue(rootNode, Map.class);

    // Close the input stream
    inputStream.close();

    return dataMap;
}
}
