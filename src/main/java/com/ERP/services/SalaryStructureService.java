
package com.ERP.services;

import com.ERP.dtos.SalaryStructureDto;
import com.ERP.entities.SalaryStructure;
import com.ERP.exceptions.IdNotFoundException;
import com.ERP.repositories.SalaryStructureRepository;
import com.ERP.servicesInter.SalaryStructureServiceInter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SalaryStructureService implements SalaryStructureServiceInter {
    private final SalaryStructureRepository salaryStructureRepository;
    private final ObjectMapper objectMapper;

    public SalaryStructureService(SalaryStructureRepository salaryStructureRepository, ObjectMapper objectMapper) {
        this.salaryStructureRepository = salaryStructureRepository;
        this.objectMapper = objectMapper;
    }

    public SalaryStructureDto createSalaryStructure(SalaryStructureDto salaryStructureDTO) {
        try {
            SalaryStructure newSalaryStructure = objectMapper.convertValue(salaryStructureDTO, SalaryStructure.class);
            SalaryStructure savedSalaryStructure = salaryStructureRepository.save(newSalaryStructure);
            return objectMapper.convertValue(savedSalaryStructure, SalaryStructureDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error adding salary structure: " + e.getMessage());
        }
    }

    @Override
    public List<SalaryStructureDto> getSalaryStructureByRole(String role) {
        try {
            List<SalaryStructure> salaryStructureList = salaryStructureRepository.findAllByRole(role);
            return Arrays.asList(objectMapper.convertValue(salaryStructureList, SalaryStructureDto[].class));
        } catch (Exception e) {
            throw new IdNotFoundException("Error finding all salaryStructure: " + e.getMessage());
        }
    }

    public SalaryStructureDto updateSalaryStructure(long structureId, SalaryStructureDto salaryStructureDTO) {
        try {
            SalaryStructure salaryStructureToUpdate = salaryStructureRepository.findById(structureId)
                    .orElseThrow(() -> new IdNotFoundException("Salary structure not found with id: " + structureId));

            salaryStructureToUpdate.setRole(salaryStructureDTO.getRole());
            salaryStructureToUpdate.setLevel(salaryStructureDTO.getLevel());
            salaryStructureToUpdate.setBaseSalary(salaryStructureDTO.getBaseSalary());

            SalaryStructure updatedSalaryStructure = salaryStructureRepository.save(salaryStructureToUpdate);
            return objectMapper.convertValue(updatedSalaryStructure, SalaryStructureDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error updating salary structure: " + e.getMessage());
        }
    }

    public SalaryStructureDto getSalaryStructureById(long structureId) {
        try {
            SalaryStructure foundSalaryStructure = salaryStructureRepository.findById(structureId)
                    .orElseThrow(() -> new IdNotFoundException("Salary structure not found with id: " + structureId));
            return objectMapper.convertValue(foundSalaryStructure, SalaryStructureDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error finding salary structure: " + e.getMessage());
        }
    }
    @Override
    public List<SalaryStructureDto> getSalaryStructureList() {
        try {
            List<SalaryStructure> salaryStructureList = salaryStructureRepository.findAll();
            return Arrays.asList(objectMapper.convertValue(salaryStructureList, SalaryStructureDto[].class));
        } catch (Exception e) {
            throw new IdNotFoundException("Error finding all salaryStructure: " + e.getMessage());
        }
    }


    public SalaryStructureDto deleteSalaryStructure(long structureId) {
        try {
            SalaryStructure salaryStructureToDelete = salaryStructureRepository.findById(structureId)
                    .orElseThrow(() -> new IdNotFoundException("Salary structure not found with id: " + structureId));
            salaryStructureRepository.deleteById(structureId);
            return objectMapper.convertValue(salaryStructureToDelete, SalaryStructureDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error deleting salary structure: " + e.getMessage());
        }
    }
}
