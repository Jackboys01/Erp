package com.ERP.servicesTest;

import com.ERP.Reporting;
import com.ERP.dtos.ProjectDto;
import com.ERP.entities.Project;
import com.ERP.entitiesTest.JsonReader;
import com.ERP.repositories.ProjectRepository;
import com.ERP.services.ProjectService;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.ERP.Reporting.extent;
import static com.ERP.Reporting.test;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class ProjectServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceTest.class);

    private ProjectRepository projectRepository;
    private ObjectMapper objectMapper;
    private ProjectService projectService;
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

    ProjectServiceTest() throws IOException {
    }

    @BeforeAll
    public static void reportSetUp()
    {
        Reporting.generateHtmlReport();
    }
    @BeforeEach
    public void setUp() {
        projectRepository = Mockito.mock(ProjectRepository.class);
        objectMapper = new ObjectMapper(); // Initialize objectMapper
        projectService = new ProjectService(projectRepository, objectMapper);
        MockitoAnnotations.openMocks(this);
        project= new Project();
        project.setProjectId(1L);
        project.setName(name);
        project.setDescription(description);
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setStatus(status);
    }

    @Test
    public void createProjectTest() {
        test=extent.createTest("Create Project Test");
        logger.info("Starting createProjectTest");
        try {
            // Mocking save method of projectRepository
            given(projectRepository.save(project)).willReturn(project);

            // Convert project1 to ProjectDto using objectMapper
            ProjectDto projectDto = objectMapper.convertValue(project, ProjectDto.class);

            // Call the method under test
            ProjectDto savedProject = projectService.addProject(projectDto);

            // Assert that the saved project is not null
            Assertions.assertThat(savedProject).isNotNull();
            logger.info("createProjectTest passed");
        } catch (Throwable t) {
            logger.error("createProjectTest failed", t);
            throw t; // Re-throw the exception to mark the test as failed
        }
    }

    @Test
    public void createAllProjectsTest()
    {
        test=extent.createTest("CreateAll Projects Test");
        logger.info("Starting createAllProjectsTest");
        try {
            List<Project> projects= new ArrayList<>();
            projects.add(project);
            projects.add(project);
            given(projectRepository.saveAll(projects)).willReturn(projects);
            List<ProjectDto> projectDtos= Arrays.asList(objectMapper.convertValue(projects,ProjectDto[].class));
            List<ProjectDto> savedProjects= projectService.addAllProject(projectDtos) ;
            Assertions.assertThat(savedProjects.get(0).getName()).isEqualTo(project.getName());
            Assertions.assertThat(savedProjects.get(1).getName()).isEqualTo(project.getName());
            logger.info("createAllProjectsTest passed");
        } catch (Throwable t) {
            logger.error("createAllProjectsTest failed", t);
            throw t;
        }
    }

    @Test
    public void updateProjectTest() {
        test=extent.createTest("Update Project Test");
        logger.info("Starting updateProjectTest");
        try {
            // Mock data
            long projectId = 1L;
            Project existingProject = project;
            ProjectDto projectDto= objectMapper.convertValue(project,ProjectDto.class);

            // Mock behavior
            given(projectRepository.findById(projectId)).willReturn(java.util.Optional.of(existingProject));
            when(projectRepository.save(any(Project.class))).thenReturn(existingProject);

            // Call the method under test
            ProjectDto updatedProject = projectService.updateProject(projectDto, projectId);

            // Assertions
            Assertions.assertThat(updatedProject).isNotNull();
            logger.info("updateProjectTest passed");
        } catch (Throwable t) {
            logger.error("updateProjectTest failed", t);
            throw t;
        }
    }

    @Test
    public void deleteProjectTest() {
        test=extent.createTest("DeleteById Project Test");
        logger.info("Starting deleteProjectTest");
        try {
            // Mock data
            long projectId = 1L;
            Project existingProject = project;
            // Mock behavior
            given(projectRepository.findById(projectId)).willReturn(java.util.Optional.of(existingProject));

            // Call the method under test
            ProjectDto deletedProject = projectService.deleteProject(projectId);

            // Assertions
            Assertions.assertThat(deletedProject).isNotNull();
            logger.info("deleteProjectTest passed");
        } catch (Throwable t) {
            logger.error("deleteProjectTest failed", t);
            throw t;
        }
    }

    @Test
    public void findProjectTest() {
        test=extent.createTest("FindById Project Test");
        logger.info("Starting findProjectTest");
        try {
            // Mock data
            long projectId = 1L;
            Project existingProject = project;

            // Mock behavior
            given(projectRepository.findById(projectId)).willReturn(java.util.Optional.of(existingProject));

            // Call the method under test
            ProjectDto foundProject = projectService.findProject(projectId);

            // Assertions
            Assertions.assertThat(foundProject).isNotNull();
            logger.info("findProjectTest passed");
        } catch (Throwable t) {
            logger.error("findProjectTest failed", t);
            throw t;
        }
    }

    @Test
    public void findAllProjectTest() {
        test=extent.createTest("FindAll Projects Test");
        logger.info("Starting findAllProjectTest");
        try {
            // Mock data
            List<Project> projectList = Arrays.asList(project, project); // Add necessary fields for projects

            // Mock behavior
            given(projectRepository.findAll()).willReturn(projectList);

            // Call the method under test
            List<ProjectDto> foundProjects = projectService.findAllProject();

            // Assertions
            Assertions.assertThat(foundProjects).isNotNull();
            Assertions.assertThat(foundProjects.size()).isEqualTo(projectList.size());
            logger.info("findAllProjectTest passed");
        } catch (Throwable t) {
            logger.error("findAllProjectTest failed", t);
            throw t;
        }
    }
    @AfterAll
    public static void tearDown()
    {
        extent.flush();
    }
}
