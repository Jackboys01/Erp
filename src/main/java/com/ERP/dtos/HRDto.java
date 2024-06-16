package com.ERP.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HRDto {
    private long hrId;
    private String name;
    private String password;
    private String role;
}
