package com.ERP.controllersTest;

import com.ERP.Reporting;
import com.ERP.config.TestSecurityConfig;
import com.ERP.controllers.ClientController;
import com.ERP.controllers.ProjectController;
import com.ERP.dtos.ProjectDto;
import com.ERP.entities.Project;
import com.ERP.entitiesTest.JsonReader;
import com.ERP.security.JwtHelper;
import com.ERP.services.ProjectService;
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
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.ERP.Reporting.extent;
import static com.ERP.Reporting.test;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = TestSecurityConfig.class)
public class ProjectControllerTest
{
    private static final Logger logger = LoggerFactory.getLogger(ProjectControllerTest.class);

    @MockBean
    ProjectService projectService;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtHelper jwtHelper;

    @Autowired
    ObjectMapper objectMapper;

    Project project;

    JsonReader jsonReader = new JsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Project");

    String name = (String) dataMap.get("name");
    String description = (String) dataMap.get("description");
    String startDateString = (String) dataMap.get("startDate");
    String endDateString = (String) dataMap.get("endDate");
    Date startDate = Date.valueOf(startDateString);
    Date endDate = Date.valueOf(endDateString);
    String status = (String) dataMap.get("status");

    public ProjectControllerTest() throws IOException {
    }

    @BeforeAll
    public static void reportSetUp()
    {
        Reporting.generateHtmlReport();
    }
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        project = new Project();
        project.setProjectId(1L);
        project.setName(name);
        project.setDescription(description);
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setStatus(status);
        when(jwtHelper.generateToken(Mockito.any())).thenReturn("mock-token");

        // Setup Security context
        SecurityContextHolder.clearContext();
    }

    @Test
    @WithMockUser
    public void testCreateProject() throws Exception {
        test=extent.createTest("Create Project Test");
        logger.info("Starting testCreateProject");
        try {
            ProjectDto projectDto =objectMapper.convertValue(project, ProjectDto.class);
            when(projectService.addProject(projectDto)).thenReturn(projectDto);
            mockMvc.perform(post("/project/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(project)))
                    .andExpect(status().isCreated());
            logger.info("testCreateProject passed");
        } catch (Throwable t) {
            logger.error("testCreateProject failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void getProjectTest() throws Exception {
        test=extent.createTest("FindById Project Test");
        logger.info("Starting getProjectTest");
        try {
            ProjectDto projectDto =objectMapper.convertValue(project, ProjectDto.class);
            long projectId = 1L;
            projectDto.setProjectId(projectId);

            when(projectService.findProject(projectId)).thenReturn(projectDto);

            mockMvc.perform(get("/project/find/{projectId}", projectId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.name").value(name));
            logger.info("getProjectTest passed");
        } catch (Throwable t) {
            logger.error("getProjectTest failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void getAllProjectsTest() throws Exception {
        test=extent.createTest("FindAll Projects Test");
        logger.info("Starting getAllProjectsTest");
        try {
            List<Project> projects= Arrays.asList(project,project);
            List<ProjectDto> projectDtos= Arrays.asList(objectMapper.convertValue(projects,ProjectDto[].class));
            projectService.addAllProject(projectDtos);
            when(projectService.findAllProject()).thenReturn(projectDtos);
            mockMvc.perform(get("/project/findAll")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value(name));
            logger.info("getAllProjectsTest passed");
        } catch (Throwable t) {
            logger.error("getAllProjectsTest failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void testUpdateProject() throws Exception {
        test=extent.createTest("Update Project Test");
        logger.info("Starting testUpdateProject");
        try {
            List<Project> projects= Arrays.asList(project,project);
            List<ProjectDto> projectDtos= (Arrays.asList(objectMapper.convertValue(projects,ProjectDto[].class)));
            projectService.addAllProject(projectDtos);

            ProjectDto projectDto=projectDtos.get(0);
            projectDto.setProjectId(1L);

            when(projectService.updateProject(projectDto, projectDto.getProjectId())).thenReturn(projectDto);
            mockMvc.perform(put("/project/update/{projectId}", projectDto.getProjectId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(projectDto)))
                    .andExpect(status().isOk());
            logger.info("testUpdateProject passed");
        } catch (Throwable t) {
            logger.error("testUpdateProject failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void testDeleteProject() throws Exception {
        test=extent.createTest("Delete ProjectById Test");
        logger.info("Starting testDeleteProject");
        try {
            ProjectDto projectDto = objectMapper.convertValue(project, ProjectDto.class);
            projectDto.setProjectId(1L);
            when(projectService.deleteProject(projectDto.getProjectId())).thenReturn(projectDto);
            mockMvc.perform(delete("/project/delete/{projectId}", projectDto.getProjectId()))
                    .andExpect(status().isOk());
            logger.info("testDeleteProject passed");
        } catch (Throwable t) {
            logger.error("testDeleteProject failed", t);
            throw t;
        }
    }

    @Test
    @WithMockUser
    public void testAddAllProjects() throws Exception {
        test=extent.createTest("AddAll Projects Test");
        logger.info("Starting testAddAllProjects");
        try {
            List<Project> projectsToAdd = Arrays.asList(project,project);
            List<ProjectDto> projectDtos= Arrays.asList(objectMapper.convertValue(projectsToAdd,ProjectDto[].class));
            when(projectService.addAllProject(projectDtos)).thenReturn(projectDtos);

            mockMvc.perform(post("/project/addAll")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(projectsToAdd)))
                    .andExpect(status().isOk());
            logger.info("testAddAllProjects passed");
        } catch (Throwable t) {
            logger.error("testAddAllProjects failed", t);
            throw t;
        }
    }
    @AfterAll
    public static void tearDown()
    {
        extent.flush();
    }
}
