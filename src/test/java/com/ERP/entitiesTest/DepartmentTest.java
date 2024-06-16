package com.ERP.entitiesTest;

import com.ERP.Reporting;
import com.ERP.entities.Department;
import com.ERP.entities.Employee;
import com.ERP.entities.Project;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static com.ERP.Reporting.extent;
import static com.ERP.Reporting.test;

@ExtendWith(MockitoExtension.class)
public class DepartmentTest
{
    private static final Logger logger = LoggerFactory.getLogger(DepartmentTest.class);

    private Department department;

    @Mock
    private Project mockProject;

    @Mock
    private Employee mockEmployee;
    Set<Project> projectSet;
    List<Employee> employees;

    JsonReader jsonReader = new JsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Department");
    String name = (String) dataMap.get("name");

    public DepartmentTest() throws IOException {
    }

    @BeforeAll
    public static void reportSetUp()
    {
        Reporting.generateHtmlReport();
    }
    @BeforeEach
    void setUp() {
        logger.info("Setting up test environment");
        MockitoAnnotations.openMocks(this);
        department= new Department();
        department.setDepartmentId(1L);
        department.setName(name);
        projectSet = new HashSet<>();
        projectSet.add(mockProject);
        department.setProjectSet(projectSet);

        employees = new ArrayList<>();
        employees.add(mockEmployee);
        department.setEmployees(employees);
    }

    @Test
    void testConstructor() {
        test=extent.createTest("Constructor Testing");
        logger.info("Testing constructor");
        Assertions.assertEquals(1L, department.getDepartmentId());
        Assertions.assertEquals(name, department.getName());
        Assertions.assertEquals(projectSet, department.getProjectSet());
        Assertions.assertEquals(employees, department.getEmployees());
    }

    @Test
    void testGetters() {
        test=extent.createTest("Getter Testing");
        logger.info("Testing getters");
        Assertions.assertEquals(1L, department.getDepartmentId());
        Assertions.assertEquals(name, department.getName());
        Assertions.assertEquals(department.getProjectSet(),projectSet);
        Assertions.assertEquals(department.getEmployees(),employees);
    }

    @Test
    void testSetters() {
        test=extent.createTest("Setter Testing");
        logger.info("Testing setters");
        department.setDepartmentId(2L);
        department.setName("Updated Department");
        Set<Project> projectSet= new HashSet<>();
        projectSet.add(mockProject);
        department.setProjectSet(projectSet);

        List<Employee> employees = new ArrayList<>();
        employees.add(mockEmployee);
        department.setEmployees(employees);

        Assertions.assertEquals(2L, department.getDepartmentId());
        Assertions.assertEquals("Updated Department", department.getName());
        Assertions.assertEquals(projectSet, department.getProjectSet());
        Assertions.assertEquals(employees, department.getEmployees());
    }
    @AfterAll
    public static void tearDown()
    {
        extent.flush();
    }
}
