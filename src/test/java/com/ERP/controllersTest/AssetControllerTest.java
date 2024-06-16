
package com.ERP.controllersTest;

import com.ERP.Reporting;
import com.ERP.config.TestSecurityConfig;
import com.ERP.controllers.AssetController;
import com.ERP.controllers.ClientController;
import com.ERP.dtos.AssetDto;
import com.ERP.entities.Asset;
import com.ERP.entitiesTest.JsonReader;
import com.ERP.security.JwtHelper;
import com.ERP.services.AssetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.ERP.Reporting.extent;
import static com.ERP.Reporting.test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(AssetController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = TestSecurityConfig.class)
public class AssetControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(AssetControllerTest.class);

    @MockBean
    AssetService assetService;

    @MockBean
    private JwtHelper jwtHelper;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

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

    public AssetControllerTest() throws IOException {
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
        // Setup mock JWT token
        when(jwtHelper.generateToken(Mockito.any())).thenReturn("mock-token");

        // Setup Security context
        SecurityContextHolder.clearContext();
    }


    @Test
    @WithMockUser
    public void testCreateAsset() throws Exception {
        test = extent.createTest("Create Asset Test");
        logger.info("Starting testCreateAsset");
        try {
            AssetDto assetDto = objectMapper.convertValue(asset, AssetDto.class);
            when(assetService.addAsset(assetDto)).thenReturn(assetDto);
            mockMvc.perform(post("/asset/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(asset)))
                    .andExpect(status().isCreated());
            logger.info("testCreateAsset passed");
        } catch (Throwable t) {
            logger.error("testCreateAsset failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void getAssetTest() throws Exception {
        test = extent.createTest("FindById Asset Test");
        logger.info("Starting getAssetTest");
        try {
            AssetDto assetDto = objectMapper.convertValue(asset, AssetDto.class);
            long assetId = 1L;
            assetDto.setAssetId(assetId);

            when(assetService.findAsset(assetId)).thenReturn(assetDto);

            mockMvc.perform(get("/asset/find/{assetId}", assetId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.name").value(name));
            logger.info("getAssetTest passed");
        } catch (Throwable t) {
            logger.error("getAssetTest failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void getAllAssetsTest() throws Exception {
        test = extent.createTest("FindAll Assets Test");
        logger.info("Starting getAllAssetsTest");
        try {
            List<Asset> assets = Arrays.asList(asset, asset);
            List<AssetDto> assetDtos = Arrays.asList(objectMapper.convertValue(assets, AssetDto[].class));
            when(assetService.findAllAsset()).thenReturn(assetDtos);
            mockMvc.perform(get("/asset/findAll")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value(name));
            logger.info("getAllAssetsTest passed");
        } catch (Throwable t) {
            logger.error("getAllAssetsTest failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void testUpdateAsset() throws Exception {
        test = extent.createTest("Update Asset Test");
        logger.info("Starting testUpdateAsset");
        try {
            List<Asset> assets = Arrays.asList(asset, asset);
            List<AssetDto> assetDtos = Arrays.asList(objectMapper.convertValue(assets, AssetDto[].class));
            AssetDto assetDto = assetDtos.get(0);
            assetDto.setAssetId(1L);

            when(assetService.updateAsset(assetDto, assetDto.getAssetId())).thenReturn(assetDto);
            mockMvc.perform(put("/asset/update/{assetId}", assetDto.getAssetId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(assetDto)))
                    .andExpect(status().isOk());
            logger.info("testUpdateAsset passed");
        } catch (Throwable t) {
            logger.error("testUpdateAsset failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void testDeleteAsset() throws Exception {
        test = extent.createTest("Delete AssetById Test");
        logger.info("Starting testDeleteAsset");
        try {
            AssetDto assetDto = objectMapper.convertValue(asset, AssetDto.class);
            assetDto.setAssetId(1L);
            when(assetService.deleteAsset(assetDto.getAssetId())).thenReturn(assetDto);
            mockMvc.perform(delete("/asset/delete/{assetId}", assetDto.getAssetId()))
                    .andExpect(status().isOk());
            logger.info("testDeleteAsset passed");
        } catch (Throwable t) {
            logger.error("testDeleteAsset failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void testAddAllAssets() throws Exception {
        test = extent.createTest("AddAll Assets Test");
        logger.info("Starting testAddAllAssets");
        try {
            List<Asset> assetsToAdd = Arrays.asList(asset, asset);
            List<AssetDto> assetDtos = Arrays.asList(objectMapper.convertValue(assetsToAdd, AssetDto[].class));
            when(assetService.addAllAsset(assetDtos)).thenReturn(assetDtos);

            mockMvc.perform(post("/asset/addAll")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(assetsToAdd)))
                    .andExpect(status().isOk());
            logger.info("testAddAllAssets passed");
        } catch (Throwable t) {
            logger.error("testAddAllAssets failed", t);
            throw t;
        }
    }

    @AfterAll
    public static void tearDown() {
        extent.flush();
    }
}
