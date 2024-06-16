package com.ERP.repositoriesTest;

import com.ERP.Reporting;
import com.ERP.entities.Department;
import com.ERP.entitiesTest.JsonReader;
import com.ERP.repositories.DepartmentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ERP.Reporting.extent;
import static com.ERP.Reporting.test;

@SpringBootTest
public class DepartmentRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentRepositoryTest.class);
    @Autowired
    private DepartmentRepository departmentRepository;

    Department department;
    JsonReader jsonReader = new JsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Department");
    String name = (String) dataMap.get("name");

    DepartmentRepositoryTest() throws IOException {
    }

    @BeforeAll
    public static void reportSetUp()
    {
        Reporting.generateHtmlReport();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        department = new Department();
        department.setDepartmentId(1L);
        department.setName(name);
    }

    @Test
    public void saveDepartmentTest() {
        test=extent.createTest("Save Department Test");
        logger.info("Starting saveDepartmentTest");
        try {
            departmentRepository.save(department);
            Assertions.assertThat(department.getDepartmentId()).isGreaterThan(0);
            Assertions.assertThat(department.getName()).isEqualTo("IT");
            logger.info("saveDepartmentTest passed");
        } catch (Throwable t) {
            logger.error("saveDepartmentTest failed", t);
            throw t; // Re-throw the exception to mark the test as failed
        }
    }

    @Test
    public void findDepartmentById() {
        test=extent.createTest("FindById Department Test");
        logger.info("Starting findDepartmentById");
        try {
            List<Department> departmentList = departmentRepository.findAll();
            Department departmentToFind = departmentList.get(0);
            Department department = departmentRepository.findById(departmentToFind.getDepartmentId()).get();
            Assertions.assertThat(department.getDepartmentId()).isEqualTo(departmentToFind.getDepartmentId());
            logger.info("findDepartmentById passed");
        } catch (Throwable t) {
            logger.error("findDepartmentById failed", t);
            throw t;
        }
    }

    @Test
    public void findAllDepartments() {
        test=extent.createTest("FindAll Departments Test");
        logger.info("Starting findAllDepartments");
        try {
            List<Department> departmentList = departmentRepository.findAll();
            Assertions.assertThat(departmentList.size()).isGreaterThan(0);
            logger.info("findAllDepartments passed");
        } catch (Throwable t) {
            logger.error("findAllDepartments failed", t);
            throw t;
        }
    }

    @Test
    public void updateDepartment() {
        test=extent.createTest("Update Department Test");
        logger.info("Starting updateDepartment");
        try {
            List<Department> departmentList = departmentRepository.findAll();
            Department departmentToFind = departmentList.get(0);
            Department department = departmentRepository.findById(departmentToFind.getDepartmentId()).get();
            department.setName("updated Department");
            Department savedDepartment = departmentRepository.save(department);
            Assertions.assertThat(savedDepartment.getName()).isEqualTo("updated Department");
            logger.info("updateDepartment passed");
        } catch (Throwable t) {
            logger.error("updateDepartment failed", t);
            throw t;
        }
    }


    @Test
    public void addAll() {
        test=extent.createTest("AddAll Departments Test");
        logger.info("Starting addAll");
        try {
            List<Department> departmentsToAdd = new ArrayList<>();
            departmentsToAdd.add(department);
            departmentsToAdd.add(department);
            List<Department> addDepartments = departmentRepository.saveAll(departmentsToAdd);
            Assertions.assertThat(addDepartments.get(0).getName()).isEqualTo(department.getName());
            Assertions.assertThat(addDepartments.get(1).getName()).isEqualTo(department.getName());
            logger.info("addAll passed");
        } catch (Throwable t) {
            logger.error("addAll failed", t);
            throw t;
        }
    }
    @AfterAll
    public static void tearDown()
    {
        extent.flush();
    }
}
