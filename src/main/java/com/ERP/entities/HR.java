package com.ERP.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "hr_table", uniqueConstraints = @UniqueConstraint(
        name = "hr_id_unique",
        columnNames = "hr_id"
))
public class HR {
    @Id
    @SequenceGenerator(
            name = "hr_sequence", // Specify the name of sequence generator
            sequenceName = "hr_sequence_name", // Specify the name of your sequence in the db
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "hr_sequence"
    )
    private long hrId;

    private String name;
    private String password;
    private String role;
}
