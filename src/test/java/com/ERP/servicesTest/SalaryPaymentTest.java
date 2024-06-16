package com.ERP.servicesTest;

import com.ERP.dtos.SalaryPaymentDto;
import com.ERP.entities.SalaryPayment;
import com.ERP.exceptions.IdNotFoundException;
import com.ERP.repositories.SalaryPaymentRepository;
import com.ERP.services.SalaryPaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SalaryPaymentTest {

    @Mock
    private SalaryPaymentRepository salaryPaymentRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SalaryPaymentService salaryPaymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSalaryPayment_ValidSalaryPaymentDto_ReturnsSalaryPaymentDto() {
        SalaryPaymentDto salaryPaymentDto = new SalaryPaymentDto(1L, 5000, Date.valueOf("2024-05-13"));
        SalaryPayment salaryPayment = new SalaryPayment(1L, null, 5000, Date.valueOf("2024-05-13"));

        when(objectMapper.convertValue(salaryPaymentDto, SalaryPayment.class)).thenReturn(salaryPayment);
        when(salaryPaymentRepository.save(salaryPayment)).thenReturn(salaryPayment);
        when(objectMapper.convertValue(salaryPayment, SalaryPaymentDto.class)).thenReturn(salaryPaymentDto);

        SalaryPaymentDto createdSalaryPaymentDto = salaryPaymentService.createSalaryPayment(salaryPaymentDto);

        assertNotNull(createdSalaryPaymentDto);
        assertEquals(salaryPaymentDto, createdSalaryPaymentDto);

        verify(salaryPaymentRepository, times(1)).save(salaryPayment);
    }

    @Test
    void updateSalaryPayment_ExistingSalaryPaymentIdAndValidSalaryPaymentDto_ReturnsUpdatedSalaryPaymentDto() {
        long paymentId = 1L;
        SalaryPaymentDto salaryPaymentDto = new SalaryPaymentDto(1L, 5000, Date.valueOf("2024-05-13"));
        SalaryPayment existingSalaryPayment = new SalaryPayment(1L, null, 5000, Date.valueOf("2024-05-13"));

        when(salaryPaymentRepository.findById(paymentId)).thenReturn(Optional.of(existingSalaryPayment));
        when(salaryPaymentRepository.save(existingSalaryPayment)).thenReturn(existingSalaryPayment);
        when(objectMapper.convertValue(existingSalaryPayment, SalaryPaymentDto.class)).thenReturn(salaryPaymentDto);

        SalaryPaymentDto updatedSalaryPaymentDto = salaryPaymentService.updateSalaryPayment(paymentId, salaryPaymentDto);

        assertNotNull(updatedSalaryPaymentDto);
        assertEquals(salaryPaymentDto, updatedSalaryPaymentDto);

        verify(salaryPaymentRepository, times(1)).findById(paymentId);
        verify(salaryPaymentRepository, times(1)).save(existingSalaryPayment);
    }

    @Test
    void updateSalaryPayment_NonExistingSalaryPaymentId_ThrowsIdNotFoundException() {
        long paymentId = 1L;
        SalaryPaymentDto salaryPaymentDto = new SalaryPaymentDto(1L, 5000, Date.valueOf("2024-05-13"));

        when(salaryPaymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> salaryPaymentService.updateSalaryPayment(paymentId, salaryPaymentDto));

        verify(salaryPaymentRepository, times(1)).findById(paymentId);
        verify(salaryPaymentRepository, never()).save(any());
    }

    @Test
    void getAllSalaryPayment_ReturnsListOfSalaryPaymentDto() {
        List<SalaryPayment> salaryPaymentList = Arrays.asList(
                new SalaryPayment(1L, null, 5000, Date.valueOf("2024-05-13")),
                new SalaryPayment(2L, null, 5000, Date.valueOf("2025-05-13"))
        );

        SalaryPaymentDto salaryPaymentDto1 = new SalaryPaymentDto(1L, null, 5000, Date.valueOf("2024-05-13"));
        SalaryPaymentDto salaryPaymentDto2 = new SalaryPaymentDto(1L, null, 5000, Date.valueOf("2024-05-13"));

        when(salaryPaymentRepository.findAll()).thenReturn(salaryPaymentList);
        when(objectMapper.convertValue(any(SalaryPayment.class), eq(SalaryPaymentDto.class)))
                .thenReturn(salaryPaymentDto1)
                .thenReturn(salaryPaymentDto2);

        List<SalaryPaymentDto> allSalaryPaymentDto = salaryPaymentService.getAllSalaryPayment();

        assertNotNull(allSalaryPaymentDto);
        assertEquals(2, allSalaryPaymentDto.size());

        verify(salaryPaymentRepository, times(1)).findAll();
        verify(objectMapper, times(2)).convertValue(any(SalaryPayment.class), eq(SalaryPaymentDto.class));
    }

    @Test
    void getSalaryPaymentById_ExistingSalaryPaymentId_ReturnsSalaryPaymentDto() {
        long paymentId = 1L;
        SalaryPayment salaryPayment = new SalaryPayment(1L, null, 5000, Date.valueOf("2024-05-13"));

        when(salaryPaymentRepository.findById(paymentId)).thenReturn(Optional.of(salaryPayment));
        when(objectMapper.convertValue(salaryPayment, SalaryPaymentDto.class)).thenReturn(new SalaryPaymentDto(paymentId, 5000, Date.valueOf("2024-05-13")));

        SalaryPaymentDto salaryPaymentDto = salaryPaymentService.getSalaryPaymentById(paymentId);

        assertNotNull(salaryPaymentDto);
        assertEquals(paymentId, salaryPaymentDto.getPaymentId());

        verify(salaryPaymentRepository, times(1)).findById(paymentId);
    }

    @Test
    void getSalaryPaymentById_NonExistingSalaryPaymentId_ThrowsIdNotFoundException() {
        long paymentId = 1L;

        when(salaryPaymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> salaryPaymentService.getSalaryPaymentById(paymentId));

        verify(salaryPaymentRepository, times(1)).findById(paymentId);
    }

    @Test
    void deleteSalaryPayment_ExistingSalaryPaymentId_ReturnsDeletedPaymentDto() {
        long paymentId = 1L;
        SalaryPayment salaryPayment = new SalaryPayment(1L, null, 5000, Date.valueOf("2024-05-13"));

        when(salaryPaymentRepository.findById(paymentId)).thenReturn(Optional.of(salaryPayment));
        doNothing().when(salaryPaymentRepository).deleteById(paymentId);
        when(objectMapper.convertValue(salaryPayment, SalaryPaymentDto.class)).thenReturn(new SalaryPaymentDto(paymentId, 5000, Date.valueOf("2024-05-13")));

        SalaryPaymentDto deletedSalaryPaymentDto = salaryPaymentService.deleteSalaryPayment(paymentId);

        assertNotNull(deletedSalaryPaymentDto);
        assertEquals(paymentId, deletedSalaryPaymentDto.getPaymentId());

        verify(salaryPaymentRepository, times(1)).findById(paymentId);
        verify(salaryPaymentRepository, times(1)).deleteById(paymentId);
    }

    @Test
    void deleteSalaryPayment_NonExistingSalaryPaymentId_ThrowsIdNotFoundException() {
        long paymentId = 1L;

        when(salaryPaymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> salaryPaymentService.deleteSalaryPayment(paymentId));

        verify(salaryPaymentRepository, times(1)).findById(paymentId);
        verify(salaryPaymentRepository, never()).deleteById(anyLong());
    }
}
