package com.ERP.servicesTest;

import com.ERP.Reporting;
import com.ERP.dtos.AssetDto;
import com.ERP.dtos.AssetDto;
import com.ERP.entities.Asset;
import com.ERP.entities.Asset;
import com.ERP.entities.Asset;
import com.ERP.entitiesTest.JsonReader;
import com.ERP.repositories.AssetRepository;
import com.ERP.repositories.AssetRepository;
import com.ERP.repositories.AssetRepository;
import com.ERP.services.AssetService;
import com.ERP.services.AssetService;
import com.ERP.services.AssetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.ERP.Reporting.extent;
import static com.ERP.Reporting.test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

public class AssetServiceTest
{
    private static final Logger logger = LoggerFactory.getLogger(AssetServiceTest.class);

    private AssetRepository assetRepository;
    private ObjectMapper objectMapper;
    private AssetService assetService;
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

    public AssetServiceTest() throws IOException {
    }

    @BeforeAll
    public static void reportSetUp() {
        Reporting.generateHtmlReport();
    }

    @BeforeEach
    void setUp() {
        assetRepository = Mockito.mock(AssetRepository.class);
        objectMapper = new ObjectMapper(); // Initialize objectMapper
        assetService = new AssetService(assetRepository, objectMapper);
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
    public void createAssetTest() {
        test=extent.createTest("Create Asset Test");
        logger.info("Starting createAssetTest");
        try {
            given(assetRepository.save(asset)).willReturn(asset);
            AssetDto assetDto = objectMapper.convertValue(asset, AssetDto.class);
            AssetDto savedAsset = assetService.addAsset(assetDto);
            Assertions.assertThat(savedAsset).isNotNull();
            logger.info("createAssetTest passed");
        } catch (Throwable t) {
            logger.error("createAssetTest failed", t);
            throw t; // Re-throw the exception to mark the test as failed
        }
    }

    @Test
    public void createAllAssetsTest()
    {
        test=extent.createTest("CreateAll Assets Test");
        logger.info("Starting createAllAssetsTest");
        try {
            List<Asset> assets= new ArrayList<>();
            assets.add(asset);
            assets.add(asset);
            given(assetRepository.saveAll(assets)).willReturn(assets);
            List<AssetDto> assetDtos= Arrays.asList(objectMapper.convertValue(assets,AssetDto[].class));
            List<AssetDto> savedAssets= assetService.addAllAsset(assetDtos) ;
            Assertions.assertThat(savedAssets.get(0).getName()).isEqualTo(asset.getName());
            Assertions.assertThat(savedAssets.get(1).getName()).isEqualTo(asset.getName());
            logger.info("createAllAssetsTest passed");
        } catch (Throwable t) {
            logger.error("createAllAssetsTest failed", t);
            throw t;
        }
    }

    @Test
    public void updateAssetTest() {
        test=extent.createTest("Update Asset Test");
        logger.info("Starting updateAssetTest");
        try {
            long assetId = 1L;
            Asset existingAsset = asset;
            AssetDto assetDto= objectMapper.convertValue(asset,AssetDto.class);
            given(assetRepository.findById(assetId)).willReturn(java.util.Optional.of(existingAsset));
            when(assetRepository.save(any(Asset.class))).thenReturn(existingAsset);
            AssetDto updatedAsset = assetService.updateAsset(assetDto, assetId);
            Assertions.assertThat(updatedAsset).isNotNull();
            logger.info("updateAssetTest passed");
        } catch (Throwable t) {
            logger.error("updateAssetTest failed", t);
            throw t;
        }
    }

    @Test
    public void deleteAssetTest() {
        test=extent.createTest("DeleteById Asset Test");
        logger.info("Starting deleteAssetTest");
        try {
            long assetId = 1L;
            Asset existingAsset= asset;
            given(assetRepository.findById(assetId)).willReturn(java.util.Optional.of(existingAsset));
            AssetDto deletedAsset = assetService.deleteAsset(assetId);
            Assertions.assertThat(deletedAsset).isNotNull();
            logger.info("deleteAssetTest passed");
        } catch (Throwable t) {
            logger.error("deleteAssetTest failed", t);
            throw t;
        }
    }

    @Test
    public void findAssetTest() {
        test=extent.createTest("FindById Asset Test");
        logger.info("Starting findAssetTest");
        try {
            long assetId = 1L;
            Asset existingAsset = asset;
            given(assetRepository.findById(assetId)).willReturn(java.util.Optional.of(existingAsset));
            AssetDto foundAsset = assetService.findAsset(assetId);
            Assertions.assertThat(foundAsset).isNotNull();
            logger.info("findAssetTest passed");
        } catch (Throwable t) {
            logger.error("findAssetTest failed", t);
            throw t;
        }
    }

    @Test
    public void findAllAssetTest() {
        test=extent.createTest("FindAll Asset Test");
        logger.info("Starting findAllAssetTest");
        try {
            List<Asset> assets = Arrays.asList(asset, asset); // Add necessary fields for projects
            given(assetRepository.findAll()).willReturn(assets);
            List<AssetDto> foundAssets = assetService.findAllAsset();
            Assertions.assertThat(foundAssets).isNotNull();
            Assertions.assertThat(foundAssets.size()).isEqualTo(assets.size());
            logger.info("findAllAssetTest passed");
        } catch (Throwable t) {
            logger.error("findAllAssetTest failed", t);
            throw t;
        }
    }
    @AfterAll
    public static void tearDown()
    {
        extent.flush();
    }
}