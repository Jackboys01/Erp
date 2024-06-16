package com.ERP.controllers;

import com.ERP.dtos.SalaryPaymentDto;
import com.ERP.services.SalaryPaymentService;
import com.ERP.utils.MyResponseGenerator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/salary-payment")
public class SalaryPaymentController {

    private final SalaryPaymentService salaryPaymentService;

    @Autowired
    public SalaryPaymentController(SalaryPaymentService salaryPaymentService) {
        this.salaryPaymentService = salaryPaymentService;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addSalaryPayment(@Valid @RequestBody SalaryPaymentDto salaryPaymentDTO) {
        SalaryPaymentDto createdSalaryPayment = salaryPaymentService.createSalaryPayment(salaryPaymentDTO);
        if (createdSalaryPayment != null) {
            return MyResponseGenerator.generateResponse(HttpStatus.CREATED, true, "Salary Payment added successfully", createdSalaryPayment);
        } else {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Failed to add Salary Payment", null);
        }
    }

    @PutMapping("/update/{paymentId}")
    public ResponseEntity<Object> updateSalaryPayment(@PathVariable Long paymentId, @Valid @RequestBody SalaryPaymentDto salaryPaymentDto) {
        SalaryPaymentDto updatedSalaryPayment  = salaryPaymentService.updateSalaryPayment(paymentId, salaryPaymentDto);
        if (updatedSalaryPayment != null) {
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Salary Payment updated successfully", updatedSalaryPayment);
        } else {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Failed to update Salary Payment", null);
        }
    }

    @GetMapping("/find/{paymentId}")
    public ResponseEntity<Object> findSalaryPayment(@PathVariable Long paymentId) {
        SalaryPaymentDto foundSalaryPayment = salaryPaymentService.getSalaryPaymentById(paymentId);
        if (foundSalaryPayment != null) {
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Salary Payment found", foundSalaryPayment);
        } else {
            return MyResponseGenerator.generateResponse(HttpStatus.NOT_FOUND, false, "SalaryPayment not found", null);
        }
    }

    @GetMapping("/findAll")
    public List<SalaryPaymentDto> findAllSalaryPayment() {
        return salaryPaymentService.getAllSalaryPayment();
    }

    @DeleteMapping("/delete/{paymentId}")
    public ResponseEntity<Object> deleteSalaryPayment(@PathVariable Long paymentId) {
        SalaryPaymentDto deletedSalaryPayment = salaryPaymentService.deleteSalaryPayment(paymentId);
        if (deletedSalaryPayment != null) {
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Salary Payment deleted successfully", deletedSalaryPayment);
        } else {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Failed to delete Salary Payment", null);
        }
    }
}
