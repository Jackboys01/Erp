package com.ERP.servicesInter;

import com.ERP.dtos.SalaryStructureDto;

import java.util.List;

public interface SalaryStructureServiceInter {
    public List<SalaryStructureDto> getSalaryStructureList();
    public SalaryStructureDto getSalaryStructureById(long structureId);
    public SalaryStructureDto createSalaryStructure(SalaryStructureDto salaryStructureDto);
    public List<SalaryStructureDto> getSalaryStructureByRole(String role);
    public SalaryStructureDto deleteSalaryStructure(long structureId);
    public SalaryStructureDto updateSalaryStructure(long structureId, SalaryStructureDto salaryStructureDto);
}
