package com.ERP.services;


import com.ERP.dtos.EmployeeDto;
import com.ERP.entities.*;
import com.ERP.exceptions.EmployeeNotFoundException;
import com.ERP.repositories.*;
import com.ERP.specifications.EmployeeSpecifications;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SalaryPaymentRepository salaryPaymentRepository;

    @Autowired
    private LeavesRepository leavesRepository;
    @Autowired
    private TaskRepository taskRepository;



    public Employee createEmployee(Employee employee,Long id) throws EmployeeNotFoundException {

        Optional <Department> department = departmentRepository.findById(id);
       if (!department.isPresent()) {
            throw new EmployeeNotFoundException("Department not found with id: " + id);
        }
//        Employee employee = new Employee();
//        BeanUtils.copyProperties(employeeDto,employee);
        JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setUsername(employee.getName());
        jwtAuthentication.setPassword(employee.getPassword());
        jwtAuthentication.setRole("EMPLOYEE");
        employee.setDepartment(department.get());
        employeeRepository.save(employee);

        return employee;
    }

    public boolean deleteEmployee(Long id) throws EmployeeNotFoundException {
       try {
           Employee employee = employeeRepository.findById(id).get();
           employeeRepository.delete(employee);
           return true;
       }
       catch (Exception e){
           throw new EmployeeNotFoundException("Department not found with id: " + id);
       }
    }


    public boolean deleteEmployeeByName(String name) throws EmployeeNotFoundException {
        try {
            List<Employee> employee = employeeRepository.findByName(name);

            employeeRepository.deleteAll(employee);
            return true;
        }
        catch (Exception e){
            throw new EmployeeNotFoundException("Department not found with id: " );
        }

    }

    public List<Employee> fetchEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        return employees;
    }


    public Employee fetchEmployeeById(Long id) {
         Optional<Employee> employee = employeeRepository.findById(id);
         if(employee.isPresent()) {
             EmployeeDto employeeDto = new EmployeeDto();
             BeanUtils.copyProperties(employee.get(), employeeDto);
             return employee.get();
         }
         else throw new NoSuchElementException();
    }


    public Employee updateEmployee(Long id, Employee employeeDto) {
      Employee employee = employeeRepository.findById(id).get();

      if(Objects.nonNull(employeeDto.getName()) && !"".equalsIgnoreCase(employeeDto.getName()))
      {
          employee.setName(employeeDto.getName());
      }

      if(Objects.nonNull(employeeDto.getRole()) && !"".equalsIgnoreCase(employeeDto.getRole()))
      {
          employee.setRole(employeeDto.getRole());
      }

      if(Objects.nonNull(employeeDto.getEmail()) && !"".equalsIgnoreCase(employeeDto.getEmail()))
      {
          employee.setEmail(employeeDto.getEmail());
      }

      employeeRepository.save(employee);

      return employeeDto;
    }

    public Employee assignTask(Long employeeId, Long taskId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        Task task = taskRepository.findById(taskId).orElseThrow();
        task.setEmployee(employee);
        taskRepository.save(task);
        return employee;
    }

    public Employee changeDepartment(Long employeeId, Long departmentId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        Department department = departmentRepository.findById(departmentId).orElseThrow();
        employee.setDepartment(department);
        return employeeRepository.save(employee);
    }



    public Employee addSalaryPayment(Long employeeId, SalaryPayment salaryPayment) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        salaryPayment.setEmployee(employee);
        salaryPaymentRepository.save(salaryPayment);
        employee.setSalaryPayment(salaryPayment);
        return employeeRepository.save(employee);
    }

    public List<Employee> searchEmployees(String name, String email, String role, Long departmentId) {
        Specification<Employee> spec = Specification.where(EmployeeSpecifications.hasName(name))
                .and(EmployeeSpecifications.hasEmail(email))
                .and(EmployeeSpecifications.hasRole(role))
                .and(EmployeeSpecifications.hasDepartmentId(departmentId));

        return employeeRepository.findAll(spec);
    }
}
