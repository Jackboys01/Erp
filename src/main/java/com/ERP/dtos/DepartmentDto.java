package com.ERP.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepartmentDto
{
    private long departmentId;
    @NotNull(message = "Name of Department cannot be null")
    @Size(min=1,max = 50,message = "min characters are 3 and maximum characters can be upto 50")
    @Pattern(regexp = "^[^\\s].*$", message = "Name of department cannot be empty")
    private String name;
}
