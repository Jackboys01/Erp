package com.ERP.entities;

import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="client")
public class Client
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @ApiModelProperty(value = "this is auto generated")
    private long clientId;
    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone is mandatory")
    @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "Phone number should be valid")
    private String phone;

    @NotBlank(message = "Address is mandatory")
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    private String address;

    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
    Set<Project> projectSet = new HashSet<>();
}