package com.ERP.dtos;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    @NotNull(message = "Name of Project cannot be null")
    @Size(min=3,max = 20,message = "min characters are 3 and maximum characters can be upto 20")
    @Pattern(regexp = "^[^\\s].*$", message = "Name of project must atleast one character")
    private String name;
    @NotNull(message = "Email cannot be empty")
    private String email;
    @NotNull(message = "Password cannot be empty")
    private String password;
    @NotNull(message = "Role cannot be empty")
    private String role;
    @NotNull(message = "Department cannot be empty")
    private String departmentName;
}
