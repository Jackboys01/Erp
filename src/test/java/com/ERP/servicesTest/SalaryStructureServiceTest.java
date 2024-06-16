package com.ERP.servicesTest;

import com.ERP.dtos.SalaryStructureDto;
import com.ERP.entities.SalaryStructure;
import com.ERP.exceptions.IdNotFoundException;
import com.ERP.repositories.SalaryStructureRepository;
import com.ERP.services.SalaryStructureService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class SalaryStructureServiceTest {

    private SalaryStructureRepository salaryStructureRepository;
    private ObjectMapper objectMapper;
    private SalaryStructureService salaryStructureService;

    @BeforeEach
    void setUp() {
        salaryStructureRepository = Mockito.mock(SalaryStructureRepository.class);
        objectMapper = new ObjectMapper(); // Initialize objectMapper
        salaryStructureService = new SalaryStructureService(salaryStructureRepository, objectMapper);
    }

    @Test
    void getSalaryStructureList() {
        // Given
        List<SalaryStructure> salaryStructureList = Arrays.asList(
                new SalaryStructure(1L, "Role 1", "senior",  1000.0),
                new SalaryStructure(2L, "Role 2", "senior", 1500.0)
        );

        given(salaryStructureRepository.findAll()).willReturn(salaryStructureList);

        // When
        List<SalaryStructureDto> result = salaryStructureService.getSalaryStructureList();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(salaryStructureList.size());
    }

    @Test
    void getSalaryStructureById() {
        // Given
        long structureId = 1L;
        SalaryStructure salaryStructure = new SalaryStructure(structureId, "Role 1", "senior", 1000.0);

        given(salaryStructureRepository.findById(structureId)).willReturn(Optional.of(salaryStructure));

        // When
        SalaryStructureDto result = salaryStructureService.getSalaryStructureById(structureId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStructureId()).isEqualTo(salaryStructure.getStructureId());
    }

    @Test
    void createSalaryStructure() {
        // Given
        SalaryStructureDto salaryStructureDto = new SalaryStructureDto(1L, "Role 1", "senior", 1000.0);
        SalaryStructure salaryStructure = new SalaryStructure(1L, "Role 1","senior", 1000.0);

        given(salaryStructureRepository.save(any(SalaryStructure.class))).willReturn(salaryStructure);

        // When
        SalaryStructureDto result = salaryStructureService.createSalaryStructure(salaryStructureDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getRole()).isEqualTo(salaryStructureDto.getRole());
    }

    @Test
    void getSalaryStructureByRole() {
        // Given
        String role = "Role 1";
        String level = "senior";
        List<SalaryStructure> salaryStructureList = Arrays.asList(
                new SalaryStructure(1L, role, level, 1000.0),
                new SalaryStructure(2L, role, level, 1500.0)
        );

        given(salaryStructureRepository.findAllByRole(role)).willReturn(salaryStructureList);

        // When
        List<SalaryStructureDto> result = salaryStructureService.getSalaryStructureByRole(role);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(salaryStructureList.size());
        assertThat(result.get(0).getRole()).isEqualTo(role);
    }

    @Test
    void deleteSalaryStructure() {
        // Given
        long structureId = 1L;
        SalaryStructure salaryStructure = new SalaryStructure(structureId, "Role 1", "senior",  1000.0);

        given(salaryStructureRepository.findById(structureId)).willReturn(Optional.of(salaryStructure));

        // When
        SalaryStructureDto result = salaryStructureService.deleteSalaryStructure(structureId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStructureId()).isEqualTo(structureId);
    }

    @Test
    void updateSalaryStructure() {
        // Given
        long structureId = 1L;
        SalaryStructureDto salaryStructureDto = new SalaryStructureDto(1L, "Updated Role", "senior",  1200.0);
        SalaryStructure existingSalaryStructure = new SalaryStructure(structureId, "Role 1", "intern", 1000.0);

        given(salaryStructureRepository.findById(structureId)).willReturn(Optional.of(existingSalaryStructure));
        given(salaryStructureRepository.save(any(SalaryStructure.class))).willReturn(existingSalaryStructure);

        // When
        SalaryStructureDto result = salaryStructureService.updateSalaryStructure(structureId, salaryStructureDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getRole()).isEqualTo(salaryStructureDto.getRole());
    }

    @Test
    void getSalaryStructureById_WithException() {
        // Given
        long structureId = 1L;

        given(salaryStructureRepository.findById(structureId)).willReturn(Optional.empty());

        // When
        // Then
        assertThrows(IdNotFoundException.class, () -> salaryStructureService.getSalaryStructureById(structureId));
    }

    @Test
    void deleteSalaryStructure_WithException() {
        // Given
        long structureId = 1L;

        given(salaryStructureRepository.findById(structureId)).willReturn(Optional.empty());

        // When
        // Then
        assertThrows(IdNotFoundException.class, () -> salaryStructureService.deleteSalaryStructure(structureId));
    }

    @Test
    void updateSalaryStructure_WithException() {
        // Given
        long structureId = 1L;
        SalaryStructureDto salaryStructureDto = new SalaryStructureDto(1L, "Updated Role", "senior", 1200.0);

        given(salaryStructureRepository.findById(structureId)).willReturn(Optional.empty());

        // When
        // Then
        assertThrows(IdNotFoundException.class, () -> salaryStructureService.updateSalaryStructure(structureId, salaryStructureDto));
    }
}
