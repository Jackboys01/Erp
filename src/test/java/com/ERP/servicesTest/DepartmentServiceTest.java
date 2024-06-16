package com.ERP.servicesTest;

import com.ERP.Reporting;
import com.ERP.dtos.DepartmentDto;
import com.ERP.entities.Department;
import com.ERP.entitiesTest.JsonReader;
import com.ERP.repositories.DepartmentRepository;
import com.ERP.services.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.ERP.Reporting.extent;
import static com.ERP.Reporting.test;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class DepartmentServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceTest.class);

    private DepartmentRepository departmentRepository;
    private ObjectMapper objectMapper;
    private DepartmentService departmentService;
    private Department department;

    JsonReader jsonReader = new JsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Department");
    String name = (String) dataMap.get("name");

    public DepartmentServiceTest() throws IOException {
    }

    @BeforeAll
    public static void reportSetUp()
    {
        Reporting.generateHtmlReport();
    }
    @BeforeEach
    public void setUp() {
        departmentRepository = Mockito.mock(DepartmentRepository.class);
        objectMapper = new ObjectMapper(); // Initialize objectMapper
        departmentService = new DepartmentService(departmentRepository, objectMapper);
        MockitoAnnotations.openMocks(this);
        department= new Department();
        department.setDepartmentId(1L);
        department.setName(name);
    }

    @Test
    public void createDepartmentTest() {
        test=extent.createTest("Create Department Test");
        logger.info("Starting createDepartmentTest");
        try {
            given(departmentRepository.save(department)).willReturn(department);
            DepartmentDto departmentDto = objectMapper.convertValue(department, DepartmentDto.class);
            DepartmentDto savedDepartment = departmentService.addDepartment(departmentDto);
            Assertions.assertThat(savedDepartment).isNotNull();
            logger.info("createDepartmentTest passed");
        } catch (Throwable t) {
            logger.error("createDepartmentTest failed", t);
            throw t; // Re-throw the exception to mark the test as failed
        }
    }

    @Test
    public void createAllDepartmentsTest()
    {
        test=extent.createTest("CreateAll Departments Test");
        logger.info("Starting createAllDepartmentsTest");
        try {
            List<Department> departments= new ArrayList<>();
            departments.add(department);
            departments.add(department);
            given(departmentRepository.saveAll(departments)).willReturn(departments);
            List<DepartmentDto> departmentDtos= Arrays.asList(objectMapper.convertValue(departments,DepartmentDto[].class));
            List<DepartmentDto> savedDepartments= departmentService.addAllDepartment(departmentDtos) ;
            Assertions.assertThat(savedDepartments.get(0).getName()).isEqualTo(department.getName());
            Assertions.assertThat(savedDepartments.get(1).getName()).isEqualTo(department.getName());
            logger.info("createAllDepartmentsTest passed");
        } catch (Throwable t) {
            logger.error("createAllDepartmentsTest failed", t);
            throw t;
        }
    }

    @Test
    public void updateDepartmentTest() {
        test=extent.createTest("Update Department Test");
        logger.info("Starting updateDepartmentTest");
        try {
            long departmentId = 1L;
            Department existingDepartment = department;
            DepartmentDto departmentDto= objectMapper.convertValue(department,DepartmentDto.class);
            given(departmentRepository.findById(departmentId)).willReturn(java.util.Optional.of(existingDepartment));
            when(departmentRepository.save(any(Department.class))).thenReturn(existingDepartment);
            DepartmentDto updatedDepartment = departmentService.updateDepartment(departmentDto, departmentId);
            Assertions.assertThat(updatedDepartment).isNotNull();
            logger.info("updateDepartmentTest passed");
        } catch (Throwable t) {
            logger.error("updateDepartmentTest failed", t);
            throw t;
        }
    }

    @Test
    public void deleteDepartmentTest() {
        test=extent.createTest("DeleteById Department Test");
        logger.info("Starting deleteDepartmentTest");
        try {
            long departmentId = 1L;
            Department existingDepartment= department;
            given(departmentRepository.findById(departmentId)).willReturn(java.util.Optional.of(existingDepartment));
            DepartmentDto deletedDepartment = departmentService.deleteDepartment(departmentId);
            Assertions.assertThat(deletedDepartment).isNotNull();
            logger.info("deleteDepartmentTest passed");
        } catch (Throwable t) {
            logger.error("deleteDepartmentTest failed", t);
            throw t;
        }
    }

    @Test
    public void findDepartmentTest() {
        test=extent.createTest("FindById Department Test");
        logger.info("Starting findDepartmentTest");
        try {
            long departmentId = 1L;
            Department existingDepartment = department;
            given(departmentRepository.findById(departmentId)).willReturn(java.util.Optional.of(existingDepartment));
            DepartmentDto foundDepartment = departmentService.findDepartment(departmentId);
            Assertions.assertThat(foundDepartment).isNotNull();
            logger.info("findDepartmentTest passed");
        } catch (Throwable t) {
            logger.error("findDepartmentTest failed", t);
            throw t;
        }
    }

    @Test
    public void findAllDepartmentTest() {
        test=extent.createTest("FindAll Department Test");
        logger.info("Starting findAllDepartmentTest");
        try {
            List<Department> departments = Arrays.asList(department, department); // Add necessary fields for projects
            given(departmentRepository.findAll()).willReturn(departments);
            List<DepartmentDto> foundDepartments = departmentService.findAllDepartment();
            Assertions.assertThat(foundDepartments).isNotNull();
            Assertions.assertThat(foundDepartments.size()).isEqualTo(departments.size());
            logger.info("findAllDepartmentTest passed");
        } catch (Throwable t) {
            logger.error("findAllDepartmentTest failed", t);
            throw t;
        }
    }
    @AfterAll
    public static void tearDown()
    {
        extent.flush();
    }
}
