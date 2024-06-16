package com.ERP.services;

import com.ERP.dtos.AssetDto;
import com.ERP.entities.Asset;
import com.ERP.exceptions.IdNotFoundException;
import com.ERP.repositories.AssetRepository;
import com.ERP.servicesInter.AssetServiceInter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
@Service
public class AssetService implements AssetServiceInter
{
    private AssetRepository assetRepository;
    private ObjectMapper objectMapper;

    public AssetService(AssetRepository assetRepository, ObjectMapper objectMapper)
    {
        this.assetRepository=assetRepository;
        this.objectMapper=objectMapper;
    }

    @Override
    public AssetDto addAsset(AssetDto assetDto) {
        try {
            Asset newAsset = objectMapper.convertValue(assetDto, Asset.class);
            assetRepository.save(newAsset);
            return objectMapper.convertValue(newAsset, AssetDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error adding asset: " + e.getMessage());
        }
    }

    @Override
    public AssetDto updateAsset(AssetDto assetDto, long assetId) {
        try {
            //get the asset which we need to update
            Asset asset= assetRepository.findById(assetId).orElseThrow(()-> new IdNotFoundException("Asset not found with id: "+assetId));

            // Update asset fields if they are not null or empty in assetDto
            if (Objects.nonNull(assetDto.getName()) && !assetDto.getName().isEmpty()) {
                asset.setName(assetDto.getName());
            }
            if (Objects.nonNull(assetDto.getDescription()) && !assetDto.getDescription().isEmpty()) {
                asset.setDescription(assetDto.getDescription());
            }
            if (Objects.nonNull(assetDto.getPurchaseDate())) {
                asset.setPurchaseDate(assetDto.getPurchaseDate());
            }
            if (Objects.nonNull(assetDto.getPurchaseCost())) {
                asset.setPurchaseCost(assetDto.getPurchaseCost());
            }
            if (Objects.nonNull(assetDto.getCurrentValue()) && !assetDto.getCurrentValue().isEmpty()) {
                asset.setCurrentValue(assetDto.getCurrentValue());
            }
            if (Objects.nonNull(assetDto.getStatus()) && !assetDto.getStatus().isEmpty()) {
                asset.setStatus(assetDto.getStatus());
            }
            assetRepository.save(asset);
            return objectMapper.convertValue(asset, AssetDto.class);
        }
        catch (Exception e) {
            throw new IdNotFoundException("Error updating asset: " + e.getMessage());
        }
    }

    @Override
    public AssetDto deleteAsset(long assetId) {
        try {
            Asset assetToDelete = assetRepository.findById(assetId)
                    .orElseThrow(() -> new IdNotFoundException("Asset not found with id: " + assetId));
            assetRepository.deleteById(assetId);
            return objectMapper.convertValue(assetToDelete, AssetDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error deleting asset: " + e.getMessage());
        }
    }

    @Override
    public AssetDto findAsset(long assetId) {
        try {
            Asset assetToSearch = assetRepository.findById(assetId)
                    .orElseThrow(() -> new IdNotFoundException("Asset not found with id: " + assetId));
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.convertValue(assetToSearch, AssetDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error finding asset: " + e.getMessage());
        }
    }

    @Override
    public List<AssetDto> addAllAsset(List<AssetDto> assetDtos) {
        try {
            List<Asset> assetList = Arrays.asList(objectMapper.convertValue(assetDtos, Asset[].class));
            assetRepository.saveAll(assetList);
            return Arrays.asList(objectMapper.convertValue(assetDtos, AssetDto[].class));
        } catch (Exception e) {
            throw new IdNotFoundException("Error adding all assets: " + e.getMessage());
        }
    }

    @Override
    public List<AssetDto> findAllAsset() {
        try {
            List<Asset> assetList = assetRepository.findAll();
            return Arrays.asList(objectMapper.convertValue(assetList, AssetDto[].class));
        } catch (Exception e) {
            throw new IdNotFoundException("Error finding all assets: " + e.getMessage());
        }
    }
}
