package com.ERP.services;

import com.ERP.dtos.SalaryPaymentDto;
import com.ERP.entities.SalaryPayment;
import com.ERP.exceptions.IdNotFoundException;
import com.ERP.repositories.SalaryPaymentRepository;
import com.ERP.servicesInter.SalaryPaymentServiceInter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SalaryPaymentService implements SalaryPaymentServiceInter {
    private final SalaryPaymentRepository salaryPaymentRepository;
    private final ObjectMapper objectMapper;

    public SalaryPaymentService(SalaryPaymentRepository salaryPaymentRepository, ObjectMapper objectMapper) {
        this.salaryPaymentRepository = salaryPaymentRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public SalaryPaymentDto createSalaryPayment(SalaryPaymentDto salaryPaymentDto) {
        try {
            SalaryPayment newSalaryPayment = objectMapper.convertValue(salaryPaymentDto, SalaryPayment.class);
            SalaryPayment savedSalaryPayment = salaryPaymentRepository.save(newSalaryPayment);
            return objectMapper.convertValue(savedSalaryPayment, SalaryPaymentDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error adding task: " + e.getMessage());
        }
    }

    @Override
    public SalaryPaymentDto updateSalaryPayment(long paymentId, SalaryPaymentDto salaryPaymentDto) {
        try {
            SalaryPayment salaryPaymentToUpdate = salaryPaymentRepository.findById(paymentId)
                    .orElseThrow(() -> new IdNotFoundException("Salary Payment not found with id: " + paymentId));

            // Update salary payment fields if they are not null or empty in the DTO
//            if (Objects.nonNull(salaryPaymentDto.getEmployee())) {
//                salaryPaymentToUpdate.setEmployee(salaryPaymentDto.getEmployee());
//            }
            if (Objects.nonNull(salaryPaymentDto.getAmount())) {
                salaryPaymentToUpdate.setAmount(salaryPaymentDto.getAmount());
            }
            if (Objects.nonNull(salaryPaymentDto.getPaymentDate())) {
                salaryPaymentToUpdate.setPaymentDate(salaryPaymentDto.getPaymentDate());
            }

            SalaryPayment updatedSalaryPayment = salaryPaymentRepository.save(salaryPaymentToUpdate);
            return objectMapper.convertValue(updatedSalaryPayment, SalaryPaymentDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error updating Salary Payment: " + e.getMessage());
        }
    }

    @Override
    public List<SalaryPaymentDto> getAllSalaryPayment() {
        try {
            List<SalaryPayment> salaryPaymentList = salaryPaymentRepository.findAll();
            List<SalaryPaymentDto> salaryPaymentDtoList = new ArrayList<>();
            for (SalaryPayment salaryPayment : salaryPaymentList) {
                salaryPaymentDtoList.add(objectMapper.convertValue(salaryPayment, SalaryPaymentDto.class));
            }
            return salaryPaymentDtoList;
        } catch (Exception e) {
            throw new IdNotFoundException("Error finding all tasks: " + e.getMessage());
        }
    }

//    @Override
//    public List<SalaryPaymentDto> getAllSalaryPayment() {
//        try {
//            List<SalaryPayment> salaryPaymentList = salaryPaymentRepository.findAll();
//            return Arrays.asList(objectMapper.convertValue(salaryPaymentList, SalaryPaymentDto[].class));
//        } catch (Exception e) {
//            throw new IdNotFoundException("Error finding all Salary Payment: " + e.getMessage());
//        }
//    }


    @Override
    public SalaryPaymentDto getSalaryPaymentById(long paymentId) {
        try {
            SalaryPayment foundSalaryPayment = salaryPaymentRepository.findById(paymentId)
                    .orElseThrow(() -> new IdNotFoundException("Salary Payment not found with id: " + paymentId));
            return objectMapper.convertValue(foundSalaryPayment, SalaryPaymentDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error finding Salary Payment: " + e.getMessage());
        }
    }

    @Override
    public SalaryPaymentDto deleteSalaryPayment(long paymentId) {
        try {
            SalaryPayment salaryPaymentToDelete = salaryPaymentRepository.findById(paymentId)
                    .orElseThrow(() -> new IdNotFoundException("Salary Payment not found with id: " + paymentId));
            salaryPaymentRepository.deleteById(paymentId);
            return objectMapper.convertValue(salaryPaymentToDelete, SalaryPaymentDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error deleting Salary Payment: " + e.getMessage());
        }
    }
}
