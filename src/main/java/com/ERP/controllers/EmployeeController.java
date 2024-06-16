package com.ERP.controllers;


import com.ERP.entities.Client;
import com.ERP.entities.Department;
import com.ERP.entities.Employee;
import com.ERP.entities.SalaryPayment;
import com.ERP.exceptions.EmployeeNotFoundException;
import com.ERP.exceptions.IdNotFoundException;
import com.ERP.services.*;
import com.ERP.utils.MyResponseGenerator;
import org.dataloader.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class EmployeeController {


    @Autowired
    public EmployeeService employeeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SalaryPaymentService salaryPaymentService;

    @Autowired
    private LeavesService leavesService;

     @PostMapping("/add/{id}")
     public ResponseEntity<Object>createEmployee(@RequestBody Employee employee, @PathVariable Long id)
     {
         try {
             return ResponseEntity.ok(employeeService.createEmployee(employee, id));
         } catch (EmployeeNotFoundException e) {
             return MyResponseGenerator.generateResponse(HttpStatus.NOT_FOUND, false, "Employee not found: " + e.getMessage(), null);
         } catch (Exception e) {
             return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Error creating employee: " + e.getMessage(), null);
         }
     }


     @DeleteMapping("/deleteById/{id}")
     public ResponseEntity<Map<String,Boolean>>deleteEmployee(@PathVariable Long id )
     {
        try {
            boolean deleted = false;
            deleted = employeeService.deleteEmployee(id);
            Map<String,Boolean>response = new HashMap<>();
            response.put("deleted",deleted);
            return ResponseEntity.ok(response);
        } catch (EmployeeNotFoundException e) {
            throw new RuntimeException(e);
        }
     }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Map<String,Boolean>>deleteEmployeeByName(@PathVariable String name )
    {

        try{
        boolean deleted = false;
        deleted = employeeService.deleteEmployeeByName(name);
        Map<String,Boolean>response = new HashMap<>();
        response.put("deleted",deleted);
        return ResponseEntity.ok(response);}
        catch(EmployeeNotFoundException e){
            throw new RuntimeException(e);
        }


    }

    @GetMapping("/find")
    public ResponseEntity<List<Employee>> fetchEmployees()
    {
      return  ResponseEntity.ok(employeeService.fetchEmployees());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Employee>fetchEmployeeById(@PathVariable Long id)
    {
        return ResponseEntity.ok(employeeService.fetchEmployeeById(id));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable ("id") Long id, @RequestBody Employee employeeDto)
    {
        employeeDto = employeeService.updateEmployee(id,employeeDto);
        return ResponseEntity.ok(employeeDto);
    }

    @PostMapping("/{employeeId}/assignTask/{taskId}")
    public ResponseEntity<Employee> assignTaskToEmployee(@PathVariable Long employeeId, @PathVariable Long taskId) {
        return ResponseEntity.ok(employeeService.assignTask(employeeId, taskId));
    }

    @PutMapping("/{employeeId}/changeDepartment/{departmentId}")
    public ResponseEntity<Employee> changeEmployeeDepartment(@PathVariable Long employeeId, @PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.changeDepartment(employeeId, departmentId));
    }

    @PostMapping("/{employeeId}/addSalaryPayment")
    public ResponseEntity<Employee> addSalaryPayment(@PathVariable Long employeeId, @RequestBody SalaryPayment salaryPayment) {
        return ResponseEntity.ok(employeeService.addSalaryPayment(employeeId, salaryPayment));
    }


    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Long departmentId) {

        List<Employee> employees = employeeService.searchEmployees(name, email, role, departmentId);
        return ResponseEntity.ok(employees);
    }
}
