package com.ERP.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="invoice")
public class Invoice
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long invoiceId;
    @Column
    private double amount;
    private Date invoiceDate;
    private String paymentStatus;

    @ManyToOne
    @JoinColumn(name="projectId")
    private Project project;
}
