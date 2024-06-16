package com.ERP.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryStructureDto {
    private long structureId;
    private String role;
    private String level;
    private double baseSalary;
}
