package com.ERP.servicesInter;

import com.ERP.dtos.DepartmentDto;

import java.util.List;

public interface DepartmentServiceInter
{

    DepartmentDto addDepartment(DepartmentDto departmentDto);
    DepartmentDto updateDepartment(DepartmentDto departmentDto,long departmentId);
    DepartmentDto deleteDepartment(long departmentId);
    DepartmentDto findDepartment(long departmentId);
    List<DepartmentDto> addAllDepartment(List<DepartmentDto> departmentDtos);
    List<DepartmentDto> findAllDepartment();
}
