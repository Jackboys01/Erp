package com.ERP.controllers;

import com.ERP.dtos.DepartmentDto;
import com.ERP.services.DepartmentService;
import com.ERP.utils.MyResponseGenerator;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
@Validated
public class DepartmentController
{
    DepartmentService departmentService;
    private final Validator validator;

    public DepartmentController(DepartmentService departmentService, LocalValidatorFactoryBean validatorFactory)
    {
        this.departmentService=departmentService;
        this.validator=validatorFactory.getValidator();
    }
    @PostMapping("/add")
    public ResponseEntity<Object> addDepartment(@Valid @RequestBody DepartmentDto departmentDto)
    {
        System.out.println("Just testing the file.");
        DepartmentDto departmentDto1=departmentService.addDepartment(departmentDto);
        if(departmentDto1!=null)
        {
            return MyResponseGenerator.generateResponse(HttpStatus.CREATED,true,"Department is added",departmentDto1);
        }
        else
        {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST,false,"Something went wrong",departmentDto1);
        }
    }

    @PutMapping("/update/{departmentId}")
    public ResponseEntity<Object> updateDepartment(@Valid @RequestBody DepartmentDto departmentDto,@PathVariable Long departmentId)
    {
        DepartmentDto departmentDto1= departmentService.updateDepartment(departmentDto,departmentId);
        if(departmentDto1!=null)
        {
            return MyResponseGenerator.generateResponse(HttpStatus.OK,true,"Department is updated successfully", departmentDto1);
        }
        else {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST,false,"Something went wrong and Department is not updated successfully",departmentDto1);
        }
    }

    @GetMapping("/find/{departmentId}")
    public ResponseEntity<Object> findDepartment(@PathVariable long departmentId)
    {
        DepartmentDto departmentToFind =departmentService.findDepartment(departmentId);
        if(departmentToFind!=null)
        {
            return MyResponseGenerator.generateResponse(HttpStatus.OK,true,"Department is found", departmentToFind);
        }
        else {
            return MyResponseGenerator.generateResponse(HttpStatus.NOT_FOUND,false,"Department is not founc",departmentToFind);
        }
    }
    @PostMapping("/addAll")
    public List<DepartmentDto> addAll(@Valid @RequestBody List<DepartmentDto> departmentDtos)
    {
        return departmentService.addAllDepartment(departmentDtos);
    }
    @DeleteMapping("/delete/{departmentId}")
    public ResponseEntity<Object> deleteDepartment(@PathVariable long departmentId)
    {
        DepartmentDto departmentDto= departmentService.deleteDepartment(departmentId);
        if(departmentDto!=null)
        {
            return MyResponseGenerator.generateResponse(HttpStatus.OK,true,"Department is Deleted Successfully",departmentDto);
        }
        else {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST,false,"Department is not Deleted Successfully",departmentDto);
        }
    }
    @GetMapping("/findAll")
    public List<DepartmentDto> findAll()
    {
        return departmentService.findAllDepartment();
    }
}
