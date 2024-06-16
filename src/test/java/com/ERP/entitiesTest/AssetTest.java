package com.ERP.entitiesTest;

import com.ERP.Reporting;
import com.ERP.entities.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Date;
import java.util.Map;

import static com.ERP.Reporting.extent;
import static com.ERP.Reporting.test;
@ExtendWith(MockitoExtension.class)
public class AssetTest
{
    private static final Logger logger = LoggerFactory.getLogger(AssetTest.class);
    Asset asset;

    JsonReader jsonReader = new JsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Asset");

    String name = (String) dataMap.get("name");
    String description = (String) dataMap.get("description");
    String purchaseDateString = (String) dataMap.get("purchaseDate");
    Date purchaseDate = Date.valueOf(purchaseDateString);
    Double purchaseCost;
    String currentValue = (String) dataMap.get("currentValue");
    String status = (String) dataMap.get("status");

    // Handling purchaseCost conversion
    {
        Object purchaseCostObject = dataMap.get("purchaseCost");
        if (purchaseCostObject instanceof Integer) {
            purchaseCost = ((Integer) purchaseCostObject).doubleValue();
        } else if (purchaseCostObject instanceof Double) {
            purchaseCost = (Double) purchaseCostObject;
        } else {
            throw new ClassCastException("Unexpected type for purchaseCost");
        }
    }

    public AssetTest() throws IOException {
    }

    @BeforeAll
    public static void reportSetUp() {
        Reporting.generateHtmlReport();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        asset = new Asset();
        asset.setAssetId(1L);
        asset.setName(name);
        asset.setDescription(description);
        asset.setPurchaseDate(purchaseDate);
        asset.setPurchaseCost(purchaseCost);
        asset.setCurrentValue(currentValue);
        asset.setStatus(status);
    }

    @Test
    void testConstructor() {
        test=extent.createTest("Constructor Testing");
        logger.info("Testing constructor");
        Assertions.assertEquals(1L, asset.getAssetId());
        Assertions.assertEquals(name, asset.getName());
        Assertions.assertEquals(description, asset.getDescription());
        Assertions.assertEquals(purchaseCost, asset.getPurchaseCost());
        Assertions.assertEquals(purchaseDate, asset.getPurchaseDate());
        Assertions.assertEquals(status, asset.getStatus());
        Assertions.assertEquals(currentValue, asset.getCurrentValue());
    }

    @Test
    void testGetters() {
        test=extent.createTest("Getter Testing");
        logger.info("Testing getters");
        Assertions.assertEquals(1L, asset.getAssetId());
        Assertions.assertEquals(name, asset.getName());
        Assertions.assertEquals(description, asset.getDescription());
        Assertions.assertEquals(purchaseCost, asset.getPurchaseCost());
        Assertions.assertEquals(purchaseDate, asset.getPurchaseDate());
        Assertions.assertEquals(status, asset.getStatus());
        Assertions.assertEquals(currentValue, asset.getCurrentValue());
    }

    @Test
    void testSetters() {
        test=extent.createTest("Setter Testing");
        logger.info("Testing setters");
        asset.setAssetId(2L);
        asset.setName("Updated Asset");
        asset.setDescription("This is an updated asset");
        asset.setPurchaseDate(Date.valueOf("2023-06-01"));
        asset.setStatus("Completed");
        asset.setPurchaseCost(1000);
        asset.setCurrentValue("New Value");
        Assertions.assertEquals(2L, asset.getAssetId());
        Assertions.assertEquals("Updated Asset", asset.getName());
        Assertions.assertEquals("This is an updated asset", asset.getDescription());
        Assertions.assertEquals(Date.valueOf("2023-06-01"), asset.getPurchaseDate());
        Assertions.assertEquals("Completed", asset.getStatus());
        Assertions.assertEquals(1000, asset.getPurchaseCost());
        Assertions.assertEquals("New Value", asset.getCurrentValue());
    }
    @AfterAll
    public static void tearDown()
    {
        extent.flush();
    }
}
