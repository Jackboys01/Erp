package com.ERP.controllersTest;

import com.ERP.controllers.SalaryPaymentController;
import com.ERP.dtos.SalaryPaymentDto;
import com.ERP.services.SalaryPaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SalaryPaymentControllerTest {
    @InjectMocks
    private SalaryPaymentController salaryPaymentController;
    @Mock
    private SalaryPaymentService salaryPaymentService;

    @Test
    void testAddSalaryPayment_Success() {
        // Prepare test data
        SalaryPaymentDto salaryPaymentDto = new SalaryPaymentDto(); // Create HRDto object as per your requirement

        // Mock HRService behavior
        when(salaryPaymentService.createSalaryPayment(salaryPaymentDto)).thenReturn(new SalaryPaymentDto()); // Assuming createSalaryPayment always returns a non-null SalaryPaymentDto

        // Call controller method
        ResponseEntity<Object> responseEntity = salaryPaymentController.addSalaryPayment(salaryPaymentDto);

        // Verify response
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testAddSalaryPayment_Failure() {
        // Prepare test data
        SalaryPaymentDto salaryPaymentDto = new SalaryPaymentDto(); // Create HRDto object as per your requirement

        // Mock HRService behavior to return null
        when(salaryPaymentService.createSalaryPayment(salaryPaymentDto)).thenReturn(null);

        // Call controller method
        ResponseEntity<Object> responseEntity = salaryPaymentController.addSalaryPayment(salaryPaymentDto);

        // Verify response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateSalaryPayment_Success() {
        // Prepare test data
        long paymentId = 1;
        SalaryPaymentDto salaryPaymentDto = new SalaryPaymentDto(); // Create HRDto object as per your requirement

        // Mock HRService behavior
        when(salaryPaymentService.updateSalaryPayment(paymentId, salaryPaymentDto)).thenReturn(new SalaryPaymentDto()); // Assuming updateHR always returns a non-null HRDto

        // Call controller method
        ResponseEntity<Object> responseEntity = salaryPaymentController.updateSalaryPayment(paymentId, salaryPaymentDto);

        // Verify response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateSalaryPayment_Failure() {
        // Prepare test data
        long paymentId = 1;
        SalaryPaymentDto salaryPaymentDto = new SalaryPaymentDto(); // Create SalaryPaymentDto object as per your requirement

        // Mock HRService behavior to return null
        when(salaryPaymentService.updateSalaryPayment(paymentId, salaryPaymentDto)).thenReturn(null);

        // Call controller method
        ResponseEntity<Object> responseEntity = salaryPaymentController.updateSalaryPayment(paymentId, salaryPaymentDto);

        // Verify response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testFindSalaryPayment_Exists() {
        // Prepare test data
        long hrId = 1;
        SalaryPaymentDto salaryPaymentDto = new SalaryPaymentDto(); // Create SalaryPaymentDto object as per your requirement

        // Mock HRService behavior
        when(salaryPaymentService.getSalaryPaymentById(hrId)).thenReturn(salaryPaymentDto); // Assuming getHRById returns a non-null HRDto when HR exists

        // Call controller method
        ResponseEntity<Object> responseEntity = salaryPaymentController.findSalaryPayment(hrId);

        // Verify response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testFindSalaryPayment_NotFound() {
        // Prepare test data
        long paymentId = 1;

        // Mock HRService behavior to return null
        when(salaryPaymentService.getSalaryPaymentById(paymentId)).thenReturn(null); // Assuming getHRById returns null when HR is not found

        // Call controller method
        ResponseEntity<Object> responseEntity = salaryPaymentController.findSalaryPayment(paymentId);

        // Verify response
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testFindAllSalaryPayment_ReturnsListOfSalaryPaymentDto() {
        // Prepare test data
        List<SalaryPaymentDto> salaryPaymentDtoList = List.of(
                new SalaryPaymentDto(1L, 5000, Date.valueOf("2024-05-13")),
                new SalaryPaymentDto(2L, 5000, Date.valueOf("2024-05-13"))
        );

        // Mock HRService behavior to return the list of HRDto objects
        when(salaryPaymentService.getAllSalaryPayment()).thenReturn(salaryPaymentDtoList);

        // Call controller method
        List<SalaryPaymentDto> actualSalaryPaymentDtoList = salaryPaymentController.findAllSalaryPayment();

        // Verify response
        assertEquals(salaryPaymentDtoList, actualSalaryPaymentDtoList);
    }

    @Test
    void testFindAllSalaryPayment_NotFound() {
        // Mock HRService behavior to return an empty list
        when(salaryPaymentService.getAllSalaryPayment()).thenReturn(Collections.emptyList());

        // Call controller method
        List<SalaryPaymentDto> actualSalaryPaymentDtoList = salaryPaymentController.findAllSalaryPayment();

        // Verify response
        assertEquals(Collections.emptyList(), actualSalaryPaymentDtoList);
    }

    @Test
    void testDeleteHR_ReturnsDeletedHRDto() {
        // Prepare test data
        long paymentId = 1;
        SalaryPaymentDto deletedSalaryPaymentDto = new SalaryPaymentDto(2L, 5000, Date.valueOf("2024-05-13"));

        // Mock HRService behavior to return the deleted HRDto object
        when(salaryPaymentService.deleteSalaryPayment(paymentId)).thenReturn(deletedSalaryPaymentDto);

        // Call controller method
        ResponseEntity<Object> responseEntity = salaryPaymentController.deleteSalaryPayment(paymentId);

        // Verify response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteHR_NotFound() {
        // Prepare test data
        long paymentId = 1;

        // Mock HRService behavior to return null when HR is not found
        when(salaryPaymentService.deleteSalaryPayment(paymentId)).thenReturn(null);

        // Call controller method
        ResponseEntity<Object> responseEntity = salaryPaymentController.deleteSalaryPayment(paymentId);

        // Verify response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
