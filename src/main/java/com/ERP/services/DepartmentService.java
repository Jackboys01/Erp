package com.ERP.services;

import com.ERP.dtos.DepartmentDto;
import com.ERP.entities.Department;
import com.ERP.exceptions.IdNotFoundException;
import com.ERP.repositories.DepartmentRepository;
import com.ERP.servicesInter.DepartmentServiceInter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class DepartmentService implements DepartmentServiceInter {
    private DepartmentRepository departmentRepository;
    private ObjectMapper objectMapper;

    public DepartmentService(DepartmentRepository departmentRepository, ObjectMapper objectMapper)
    {
        this.departmentRepository=departmentRepository;
        this.objectMapper=objectMapper;
    }
    @Override
    public DepartmentDto addDepartment(DepartmentDto departmentDto) {
        try {
            Department newDepartment = objectMapper.convertValue(departmentDto, Department.class);
            departmentRepository.save(newDepartment);
            return objectMapper.convertValue(newDepartment, DepartmentDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error adding department: " + e.getMessage());
        }
    }

    @Override
    public DepartmentDto updateDepartment(DepartmentDto departmentDto, long departmentId) {
        try {
            //get the department which we need to update
            Department department= departmentRepository.findById(departmentId).orElseThrow(()-> new IdNotFoundException("Department not found with id: "+departmentId));

            // Update department fields if they are not null or empty in departmentDto
            if (Objects.nonNull(departmentDto.getName()) && !departmentDto.getName().isEmpty()) {
                department.setName(departmentDto.getName());
            }
            departmentRepository.save(department);
            return objectMapper.convertValue(department, DepartmentDto.class);
        }
        catch (Exception e) {
            throw new IdNotFoundException("Error updating department: " + e.getMessage());
        }
    }

    @Override
    public DepartmentDto deleteDepartment(long departmentId) {
        try {
            Department departmentToDelete = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new IdNotFoundException("Department not found with id: " + departmentId));
            departmentRepository.deleteById(departmentId);
            return objectMapper.convertValue(departmentToDelete, DepartmentDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error deleting department: " + e.getMessage());
        }
    }

    @Override
    public DepartmentDto findDepartment(long departmentId) {
        try {
            Department departmentToSearch = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new IdNotFoundException("Department not found with id: " + departmentId));
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.convertValue(departmentToSearch, DepartmentDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error finding department: " + e.getMessage());
        }
    }

    @Override
    public List<DepartmentDto> addAllDepartment(List<DepartmentDto> departmentDtos) {
        try {
            List<Department> departmentList= Arrays.asList(objectMapper.convertValue(departmentDtos, Department[].class));
            departmentRepository.saveAll(departmentList);
            return Arrays.asList(objectMapper.convertValue(departmentDtos, DepartmentDto[].class));
        } catch (Exception e) {
            throw new IdNotFoundException("Error adding all departments: " + e.getMessage());
        }
    }

    @Override
    public List<DepartmentDto> findAllDepartment() {
        try {
            List<Department> departmentList = departmentRepository.findAll();
            return Arrays.asList(objectMapper.convertValue(departmentList, DepartmentDto[].class));
        } catch (Exception e) {
            throw new IdNotFoundException("Error finding all departments: " + e.getMessage());
        }
    }
}
