package com.ERP.repositoriesTest;

import com.ERP.Reporting;
import com.ERP.entities.Asset;
import com.ERP.entitiesTest.JsonReader;
import com.ERP.repositories.AssetRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ERP.Reporting.extent;
import static com.ERP.Reporting.test;

@SpringBootTest
public class AssetRepositoryTest
{
    private static final Logger logger = LoggerFactory.getLogger(AssetRepositoryTest.class);
    @Autowired
    private AssetRepository assetRepository;
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

    public AssetRepositoryTest() throws IOException {
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
    public void saveAssetTest() {
        test=extent.createTest("Save Asset Test");
        logger.info("Starting saveAssetTest");
        try {
            assetRepository.save(asset);
            Assertions.assertThat(asset.getAssetId()).isGreaterThan(0);
            Assertions.assertThat(asset.getName()).isEqualTo(name);
            logger.info("saveAssetTest passed");
        } catch (Throwable t) {
            logger.error("saveAssetTest failed", t);
            throw t; // Re-throw the exception to mark the test as failed
        }
    }

    @Test
    public void findAssetById() {
        test=extent.createTest("FindById Asset Test");
        logger.info("Starting findAssetById");
        try {
            List<Asset> assets = assetRepository.findAll();
            Asset assetToFind = assets.get(0);
            Asset asset = assetRepository.findById(assetToFind.getAssetId()).get();
            Assertions.assertThat(asset.getAssetId()).isEqualTo(assetToFind.getAssetId());
            logger.info("findAssetById passed");
        } catch (Throwable t) {
            logger.error("findAssetById failed", t);
            throw t;
        }
    }

    @Test
    public void findAllAssets() {
        test=extent.createTest("FindAll Assets Test");
        logger.info("Starting findAllAssets");
        try {
            addAll();
            List<Asset> assets = assetRepository.findAll();
            Assertions.assertThat(assets.size()).isGreaterThan(0);
            logger.info("findAllAssets passed");
        } catch (Throwable t) {
            logger.error("findAllAssets failed", t);
            throw t;
        }
    }

    @Test
    public void updateAsset() {
        test=extent.createTest("Update Asset Test");
        logger.info("Starting updateAsset");
        try {
            List<Asset> assets = assetRepository.findAll();
            Asset assetToFind = assets.get(0);
            Asset asset = assetRepository.findById(assetToFind.getAssetId()).get();
            asset.setStatus("done...");
            Asset savedAsset = assetRepository.save(asset);
            Assertions.assertThat(savedAsset.getStatus()).isEqualTo("done...");
            logger.info("updateAsset passed");
        } catch (Throwable t) {
            logger.error("updateAsset failed", t);
            throw t;
        }
    }

    @Test
    public void deleteAsset() {
        test=extent.createTest("DeleteById Asset Test");
        logger.info("Starting deleteAsset");
        try {
            List<Asset> assets = assetRepository.findAll();
            Asset assetToDelete = assets.get(0);
            assetRepository.delete(assetToDelete);
            Asset deletedAsset = null;
            Optional<Asset> optionalAsset = assetRepository.findById(assetToDelete.getAssetId());
            if (optionalAsset.isPresent()) {
                deletedAsset = optionalAsset.get();
            }
            Assertions.assertThat(deletedAsset).isNull();
            logger.info("deleteAsset passed");
        } catch (Throwable t) {
            logger.error("deleteAsset failed", t);
            throw t;
        }
    }

    @Test
    public void addAll() {
        test=extent.createTest("AddAll Assets Test");
        logger.info("Starting addAll");
        try {
            List<Asset> assetsToAdd = new ArrayList<>();
            assetsToAdd.add(asset);
            assetsToAdd.add(asset);
            List<Asset> addAssets = assetRepository.saveAll(assetsToAdd);
            Assertions.assertThat(addAssets.get(0).getName()).isEqualTo(asset.getName());
            Assertions.assertThat(addAssets.get(1).getName()).isEqualTo(asset.getName());
            logger.info("addAll passed");
        } catch (Throwable t) {
            logger.error("addAll failed", t);
            throw t;
        }
    }

//    @Test
//    public void findByNameTest() {
//        test=extent.createTest("FindByName Asset Test");
//        logger.info("Starting findByNameTest");
//        try {
//            List<Asset> assets = assetRepository.findAll();
//            Asset assetToFind = assets.get(0);
//            Asset asset = assetRepository.findById(assetToFind.getName()).get(0);
//            Assertions.assertThat(asset.getName()).isEqualTo(assetToFind.getName());
//            logger.info("findByNameTest passed");
//        } catch (Throwable t) {
//            logger.error("findByNameTest failed", t);
//            throw t;
//        }
//    }
    @AfterAll
    public static void tearDown()
    {
        extent.flush();
    }
}
