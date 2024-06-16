package com.ERP.controllersTest;

import com.ERP.Reporting;
import com.ERP.config.SecurityConfig;
import com.ERP.config.TestSecurityConfig;
import com.ERP.controllers.ClientController;
import com.ERP.controllers.DepartmentController;
import com.ERP.dtos.DepartmentDto;
import com.ERP.entities.Department;
import com.ERP.entitiesTest.JsonReader;
import com.ERP.security.JwtHelper;
import com.ERP.services.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.ERP.Reporting.extent;
import static com.ERP.Reporting.test;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = TestSecurityConfig.class)
public class DepartmentControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentControllerTest.class);
    @MockBean
    private JwtHelper jwtHelper;
    @MockBean
    DepartmentService departmentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    Department department;

    JsonReader jsonReader = new JsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Department");

    String name = (String) dataMap.get("name");

    public DepartmentControllerTest() throws IOException {
    }

    @BeforeAll
    public static void reportSetUp() {
        Reporting.generateHtmlReport();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        department = new Department();
        department.setDepartmentId(1L);
        department.setName(name);

        // Setup mock JWT token
        when(jwtHelper.generateToken(Mockito.any())).thenReturn("mock-token");

        // Setup Security context
        SecurityContextHolder.clearContext();
    }

    @Test
    @WithMockUser // Use a mock user with default roles
    public void testCreateDepartment() throws Exception {
        test = extent.createTest("Create Department Test");
        logger.info("Starting testCreateDepartment");
        try {
            DepartmentDto departmentDto = objectMapper.convertValue(department, DepartmentDto.class);
            when(departmentService.addDepartment(departmentDto)).thenReturn(departmentDto);
            mockMvc.perform(post("/department/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(departmentDto)))
                    .andExpect(status().isCreated());
            logger.info("testCreateDepartment passed");
        } catch (Throwable t) {
            logger.error("testCreateDepartment failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void getDepartmentTest() throws Exception {
        test = extent.createTest("Find DepartmentById Test");
        logger.info("Starting getDepartmentTest");
        try {
            DepartmentDto departmentDto = objectMapper.convertValue(department, DepartmentDto.class);
            long departmentId = 1L;
            departmentDto.setDepartmentId(departmentId);
            when(departmentService.findDepartment(departmentId)).thenReturn(departmentDto);

            mockMvc.perform(get("/department/find/{departmentId}", departmentId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.name").value(name));
            logger.info("getDepartmentTest passed");
        } catch (Throwable t) {
            logger.error("getDepartmentTest failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void getAllDepartmentsTest() throws Exception {
        test = extent.createTest("FindAll Departments Test");
        logger.info("Starting getAllDepartmentsTest");
        try {
            List<Department> departments = Arrays.asList(department, department);
            List<DepartmentDto> departmentDtos = Arrays.asList(objectMapper.convertValue(departments, DepartmentDto[].class));
            departmentService.addAllDepartment(departmentDtos);
            when(departmentService.findAllDepartment()).thenReturn(departmentDtos);

            mockMvc.perform(get("/department/findAll")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value(name));
            logger.info("getAllDepartmentsTest passed");
        } catch (Throwable t) {
            logger.error("getAllDepartmentsTest failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void testUpdateDepartment() throws Exception {
        test = extent.createTest("Update Department Test");
        logger.info("Starting testUpdateDepartment");
        try {
            List<Department> departments = Arrays.asList(department, department);
            List<DepartmentDto> departmentDtos = Arrays.asList(objectMapper.convertValue(departments, DepartmentDto[].class));
            departmentService.addAllDepartment(departmentDtos);
            DepartmentDto departmentDto = departmentDtos.get(0);
            departmentDto.setDepartmentId(1L);

            when(departmentService.updateDepartment(departmentDto, departmentDto.getDepartmentId())).thenReturn(departmentDto);
            mockMvc.perform(put("/department/update/{departmentId}", departmentDto.getDepartmentId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(departmentDto)))
                    .andExpect(status().isOk());
            logger.info("testUpdateDepartment passed");
        } catch (Throwable t) {
            logger.error("testUpdateDepartment failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void testDeleteDepartment() throws Exception {
        test = extent.createTest("Delete DepartmentById Test");
        logger.info("Starting testDeleteDepartment");
        try {
            DepartmentDto departmentDto = objectMapper.convertValue(department, DepartmentDto.class);
            departmentDto.setDepartmentId(1L);
            when(departmentService.deleteDepartment(departmentDto.getDepartmentId())).thenReturn(departmentDto);

            mockMvc.perform(delete("/department/delete/{departmentId}", departmentDto.getDepartmentId()))
                    .andExpect(status().isOk());
            logger.info("testDeleteDepartment passed");
        } catch (Throwable t) {
            logger.error("testDeleteDepartment failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void testAddAllDepartments() throws Exception {
        test = extent.createTest("AddAll Departments Test");
        logger.info("Starting testAddAllDepartments");
        try {
            List<Department> departments = Arrays.asList(department, department);
            List<DepartmentDto> departmentDtos = Arrays.asList(objectMapper.convertValue(departments, DepartmentDto[].class));
            when(departmentService.addAllDepartment(departmentDtos)).thenReturn(departmentDtos);

            mockMvc.perform(post("/department/addAll")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(departmentDtos)))
                    .andExpect(status().isOk());
            logger.info("testAddAllDepartments passed");
        } catch (Throwable t) {
            logger.error("testAddAllDepartments failed", t);
            throw t;
        }
    }

    @AfterAll
    public static void tearDown() {
        extent.flush();
    }
}
