package com.ERP.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryPaymentDto {
    private Long paymentId;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private double amount;

    @NotNull(message = "Payment date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date paymentDate;

    public SalaryPaymentDto(Long paymentId, double amount, Date paymentDate) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }
}
