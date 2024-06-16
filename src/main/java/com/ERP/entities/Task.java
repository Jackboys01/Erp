package com.ERP.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="task")
public class Task
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long taskId;
    @Column
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private String status;

    @ManyToOne
    @JoinColumn(name="projectId")
    @JsonBackReference
    private Project assignTo;


    @ManyToOne
    @JoinColumn(name="employeeId")
    @JsonBackReference
    private Employee employee;
}

