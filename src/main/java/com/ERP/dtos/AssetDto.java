package com.ERP.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetDto
{
        private long assetId;
        @NotNull(message = "Name of Asset cannot be null")
        @Size(min=1,max = 50,message = "min characters are 1 and maximum characters can be upto 50")
        @Pattern(regexp = "^[^\\s].*$", message = "Name of Asset cannot be empty")
        private String name;
        @NotNull(message = "Description of Asset cannot be null")
        @Size(min=1,max = 200,message = "min characters are 3 and maximum characters can be upto 50")
        @Pattern(regexp = "^[^\\s].*$", message = "Description of Asset cannot be empty")
        private String description;

        @NotNull(message = "purchaseDate of Asset cannot be null")
        private Date purchaseDate;

        @NotNull(message = "PurchaseCost of Asset cannot be null")
        private double purchaseCost;

        @NotNull(message = "CurrentValue of Asset cannot be null")
        @Pattern(regexp = "^[^\\s].*$", message = "Current Value of Asset cannot be empty")
        private String currentValue;
        @NotNull(message = "Status of Asset cannot be null")
        @Pattern(regexp = "^[^\\s].*$", message = "Status of Asset cannot be empty")
        private String status;
}
