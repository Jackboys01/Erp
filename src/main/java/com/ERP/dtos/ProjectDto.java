package com.ERP.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectDto
{
    private long projectId;
    @NotNull(message = "Name of Project cannot be null")
    @Size(min=1,max = 50,message = "min characters are 1 and maximum characters can be upto 50")
    @Pattern(regexp = "^[^\\s].*$", message = "Name of project cannot be empty")
    private String name;
    @NotNull(message = "Description cannot be empty")
    @Size(min=1,max = 200,message = "min characters are 3 and maximum characters can be upto 50")
    @Pattern(regexp = "^[^\\s].*$", message = "Description of project cannot be empty")
    private String description;
    @NotNull(message = "start Date cannot be empty")
    private Date startDate;
    @NotNull(message = "end Date cannot be empty")
    private Date endDate;
    @NotNull(message = "status Date cannot be empty")
    @Size(min=1,max = 50,message = "min characters are 3 and maximum characters can be upto 50")
    @Pattern(regexp = "^[^\\s].*$", message = "Status of project cannot be empty")
    private String status;
}
