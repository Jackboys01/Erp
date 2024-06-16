package com.ERP.controllers;

import com.ERP.dtos.SalaryStructureDto;
import com.ERP.services.SalaryStructureService;
import com.ERP.utils.MyResponseGenerator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salary-structure")
public class SalaryStructureController {

    private final SalaryStructureService salaryStructureService;

    @Autowired
    public SalaryStructureController(SalaryStructureService salaryStructureService) {
        this.salaryStructureService = salaryStructureService;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addSalaryStructure(@Valid @RequestBody SalaryStructureDto salaryStructureDTO) {
        SalaryStructureDto createdSalaryStructure = salaryStructureService.createSalaryStructure(salaryStructureDTO);
        if (createdSalaryStructure != null) {
            return MyResponseGenerator.generateResponse(HttpStatus.CREATED, true, "Salary structure added successfully", createdSalaryStructure);
        } else {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Failed to add salary structure", null);
        }
    }

    @PutMapping("/update/{structureId}")
    public ResponseEntity<Object> updateSalaryStructure(@PathVariable long structureId, @Valid @RequestBody SalaryStructureDto salaryStructureDTO) {
        SalaryStructureDto updatedSalaryStructure = salaryStructureService.updateSalaryStructure(structureId, salaryStructureDTO);
        if (updatedSalaryStructure != null) {
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Salary structure updated successfully", updatedSalaryStructure);
        } else {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Failed to update salary structure", null);
        }
    }

    @GetMapping("/find/{structureId}")
    public ResponseEntity<Object> findSalaryStructure(@PathVariable long structureId) {
        SalaryStructureDto foundSalaryStructure = salaryStructureService.getSalaryStructureById(structureId);
        if (foundSalaryStructure != null) {
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Salary structure found", foundSalaryStructure);
        } else {
            return MyResponseGenerator.generateResponse(HttpStatus.NOT_FOUND, false, "Salary structure not found", null);
        }
    }

    @GetMapping("/findAll")
    public List<SalaryStructureDto> findAllSalaryStructure() {
        return salaryStructureService.getSalaryStructureList();
    }

    @GetMapping("/findAllByRole/{role}")
    public List<SalaryStructureDto> findAllSalaryStructureByRole(@PathVariable String role) {
        return salaryStructureService.getSalaryStructureByRole(role);
    }

    @DeleteMapping("/delete/{structureId}")
    public ResponseEntity<Object> deleteSalaryStructure(@PathVariable long structureId) {
        SalaryStructureDto deletedSalaryStructure = salaryStructureService.deleteSalaryStructure(structureId);
        if (deletedSalaryStructure != null) {
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Salary structure deleted successfully", deletedSalaryStructure);
        } else {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Failed to delete salary structure", null);
        }
    }
}
