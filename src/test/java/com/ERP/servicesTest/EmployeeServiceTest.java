package com.ERP.servicesTest;


import com.ERP.dtos.EmployeeDto;
import com.ERP.entities.Department;
import com.ERP.entities.Employee;
import com.ERP.exceptions.EmployeeNotFoundException;
import com.ERP.repositories.DepartmentRepository;
import com.ERP.repositories.EmployeeRepository;
import com.ERP.services.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private DepartmentRepository departmentRepository;


    @InjectMocks
    private EmployeeService employeeService;
    AutoCloseable autoCloseable;
    Employee employee;

   Department department;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        department = Department.builder()
                .departmentId(1L)
                .name("Your Department Name")
                .build();
        employee = Employee.builder()
                .id(1L)
                .name("daksh")
                .email("dakshmalik437@gmail.com")
                .role("Manager")
                .department(department)
                .task(new ArrayList<>()) // Initialize tasks list
                .salaryPayment(null) // Set salaryPayment, assuming it's nullable
                .leaves(new ArrayList<>()) // Initialize leaves list
                .build();

//       employeeDto = new EmployeeDto("dash", "dakshmalik437@gmail.com", "Manager", "IT");

    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void createEmployee() throws EmployeeNotFoundException {
        mock(EmployeeRepository.class);
//        mock(EmployeeDto.class);
        mock(Employee.class);

        mock(DepartmentRepository.class);
//        mock(EmployeeDto.class);
        mock(Department.class);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        // Mocking behavior of employeeRepository.save()
        when(employeeRepository.save(employee)).thenReturn(employee);

        // Assert that the returned EmployeeDto matches the input EmployeeDto

        assertThat(employeeService.createEmployee(employee,1L)).isEqualTo(employee);
    }

    @Test
    void deleteEmployee() throws EmployeeNotFoundException {
        mock(EmployeeRepository.class, Mockito.CALLS_REAL_METHODS);
//        mock(EmployeeDto.class);
        mock(Employee.class);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        doAnswer(Answers.CALLS_REAL_METHODS).when(employeeRepository).delete(employee);
        assertThat(employeeService.deleteEmployee(1L)).isEqualTo(true);
    }

    @Test
    void deleteEmployeeByName() throws EmployeeNotFoundException {
        mock(EmployeeRepository.class, Mockito.CALLS_REAL_METHODS);
//        mock(EmployeeDto.class);
        mock(Employee.class);
        when(employeeRepository.findByName("daksh")).thenReturn(new ArrayList<Employee>(Collections.singleton(employee)));
        doAnswer(Answers.CALLS_REAL_METHODS).when(employeeRepository).deleteAll(new ArrayList<Employee>(Collections.singleton(employee)));
        assertThat(employeeService.deleteEmployeeByName("daksh")).isEqualTo(true);
    }

    @Test
    void fetchEmployees() {
        mock(EmployeeRepository.class);
//        mock(EmployeeDto.class);
        mock(Employee.class);

        when(employeeRepository.findAll()).thenReturn(new ArrayList<Employee>(Collections.singleton(employee)));

        assertThat(employeeService.fetchEmployees().get(0)).isEqualTo(employee);
    }

    @Test
    void fetchEmployeeById() {
        mock(EmployeeRepository.class);
//        mock(EmployeeDto.class);
        mock(Employee.class);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        assertThat(employeeService.fetchEmployeeById(1L).getName()).isEqualTo(employee.getName());
    }

    @Test
    void updateEmployee() {
        mock(EmployeeRepository.class);
//        mock(EmployeeDto.class);
        mock(Employee.class);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        // Mocking behavior of employeeRepository.save()
        when(employeeRepository.save(employee)).thenReturn(employee);

        // Assert that the returned EmployeeDto matches the input EmployeeDto

        assertThat(employeeService.updateEmployee(1L,employee)).isEqualTo(employee);
    }
}