package com.ERP.servicesInter;

import com.ERP.dtos.SalaryPaymentDto;

import java.util.List;

public interface SalaryPaymentServiceInter {
    SalaryPaymentDto createSalaryPayment(SalaryPaymentDto salaryPaymentDto);
    SalaryPaymentDto getSalaryPaymentById(long paymentId);
    List<SalaryPaymentDto> getAllSalaryPayment();
    SalaryPaymentDto deleteSalaryPayment(long paymentId);
    SalaryPaymentDto updateSalaryPayment(long paymentId, SalaryPaymentDto salaryPaymentDto);
}
