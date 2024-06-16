package com.ERP.servicesInter;

import com.ERP.dtos.AssetDto;
import java.util.List;

public interface AssetServiceInter
{
    AssetDto addAsset(AssetDto assetDto);
    AssetDto updateAsset(AssetDto assetDto,long assetId);
    AssetDto deleteAsset(long assetId);
    AssetDto findAsset(long assetId);
    List<AssetDto> addAllAsset(List<AssetDto> assetDtos);
    List<AssetDto> findAllAsset();
}
