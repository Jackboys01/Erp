package com.ERP.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="asset")
public class Asset
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long assetId;
    @Column
    private String name;
    private String description;
    private Date purchaseDate;
    private double purchaseCost;
    private String currentValue;
    private String status;
}
