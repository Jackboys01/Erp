package com.ERP.controllers;

import com.ERP.dtos.AssetDto;
import com.ERP.services.AssetService;
import com.ERP.utils.MyResponseGenerator;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/asset")
@Validated
public class AssetController
{
    AssetService assetService;
    private final Validator validator;

    public AssetController(AssetService assetService, LocalValidatorFactoryBean validatorFactory)
    {
        this.assetService=assetService;
        this.validator = validatorFactory.getValidator();
    }
    @PostMapping("/add")
    public ResponseEntity<Object> addAsset(@Valid @RequestBody AssetDto assetDto)
    {
        AssetDto newAsset=assetService.addAsset(assetDto);
        if(assetDto!=null)
        {
            return MyResponseGenerator.generateResponse(HttpStatus.CREATED,true,"Asset is added",newAsset);
        }
        else
        {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST,false,"Something went wrong",newAsset);
        }
    }

    @PutMapping("/update/{assetId}")
    public ResponseEntity<Object> updateAsset( @Valid @RequestBody AssetDto assetDto,@PathVariable Long assetId)
    {
        AssetDto assetDto1= assetService.updateAsset(assetDto,assetId);
        if(assetDto1!=null)
        {
            return MyResponseGenerator.generateResponse(HttpStatus.OK,true,"Asset is updated successfully", assetDto1);
        }
        else {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST,false,"Something went wrong and Asset is not updated successfully",assetDto1);
        }
    }

    @GetMapping("/find/{assetId}")
    public ResponseEntity<Object> findAsset(@PathVariable long assetId)
    {
        AssetDto assetTofind =assetService.findAsset(assetId);
        if(assetTofind!=null)
        {
            return MyResponseGenerator.generateResponse(HttpStatus.OK,true,"Asset is found", assetTofind);
        }
        else {
            return MyResponseGenerator.generateResponse(HttpStatus.NOT_FOUND,false,"Asset is not founc",assetTofind);
        }
    }

    @PostMapping("/addAll")
    public List<AssetDto> addAll(@Valid @RequestBody List< AssetDto> assetDtos)
    {
        return assetService.addAllAsset(assetDtos);
    }

    @DeleteMapping("/delete/{assetId}")
    public ResponseEntity<Object> deleteAsset(@PathVariable long assetId)
    {
        AssetDto assetDto= assetService.deleteAsset(assetId);
        if(assetDto!=null)
        {
            return MyResponseGenerator.generateResponse(HttpStatus.OK,true,"Asset is Deleted Successfully",assetDto);
        }
        else {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST,false,"Asset is not Deleted Successfully",assetDto);
        }
    }
    @GetMapping("/findAll")
    public List<AssetDto> findAll()
    {
        return assetService.findAllAsset();
    }
}
