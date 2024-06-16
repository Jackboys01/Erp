package com.ERP.repositoriesTest;

import com.ERP.Reporting;
import com.ERP.entities.Project;
import com.ERP.entitiesTest.JsonReader;
import com.ERP.repositories.ProjectRepository;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ERP.Reporting.extent;
import static com.ERP.Reporting.test;

@SpringBootTest
public class ProjectRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(ProjectRepositoryTest.class);
    @Autowired
    private ProjectRepository projectRepository;

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

    ProjectRepositoryTest() throws IOException {
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
    }

    @Test
    public void saveProjectTest() {
        test=extent.createTest("Save Project Test");
        logger.info("Starting saveProjectTest");
        try {
            projectRepository.save(project);
            Assertions.assertThat(project.getProjectId()).isGreaterThan(0);
            Assertions.assertThat(project.getName()).isEqualTo(name);
            logger.info("saveProjectTest passed");
        } catch (Throwable t) {
            logger.error("saveProjectTest failed", t);
            throw t; // Re-throw the exception to mark the test as failed
        }
    }

    @Test
    public void findProjectById() {
        test=extent.createTest("FindById Project Test");
        logger.info("Starting findProjectById");
        try {
            List<Project> projects = projectRepository.findAll();
            Project projectToFind = projects.get(0);
            Project project = projectRepository.findById(projectToFind.getProjectId()).get();
            Assertions.assertThat(project.getProjectId()).isEqualTo(projectToFind.getProjectId());
            logger.info("findProjectById passed");
        } catch (Throwable t) {
            logger.error("findProjectById failed", t);
            throw t;
        }
    }

    @Test
    public void findAllProjects() {
        test=extent.createTest("FindAll Projects Test");
        logger.info("Starting findAllProjects");
        try {
            List<Project> projects = projectRepository.findAll();
            Assertions.assertThat(projects.size()).isGreaterThan(0);
            logger.info("findAllProjects passed");
        } catch (Throwable t) {
            logger.error("findAllProjects failed", t);
            throw t;
        }
    }

    @Test
    public void updateProject() {
        test=extent.createTest("Update Project Test");
        logger.info("Starting updateProject");
        try {
            List<Project> projects = projectRepository.findAll();
            Project projectToFind = projects.get(0);
            Project project = projectRepository.findByName(projectToFind.getName()).get(0);
            project.setStatus("done...");
            Project savedProject = projectRepository.save(project);
            Assertions.assertThat(savedProject.getStatus()).isEqualTo("done...");
            logger.info("updateProject passed");
        } catch (Throwable t) {
            logger.error("updateProject failed", t);
            throw t;
        }
    }

    @Test
    public void deleteProject() {
        test=extent.createTest("DeleteById Project Test");
        logger.info("Starting deleteProject");
        try {
            List<Project> projects = projectRepository.findAll();
            Project projectToDelete = projects.get(0);
            projectRepository.delete(projectToDelete);
            Project deletedProject = null;
            Optional<Project> optionalProject = projectRepository.findById(projectToDelete.getProjectId());
            if (optionalProject.isPresent()) {
                deletedProject = optionalProject.get();
            }
            Assertions.assertThat(deletedProject).isNull();
            logger.info("deleteProject passed");
        } catch (Throwable t) {
            logger.error("deleteProject failed", t);
            throw t;
        }
    }

    @Test
    public void addAll() {
        test=extent.createTest("AddAll Projects Test");
        logger.info("Starting addAll");
        try {
            List<Project> projectsToAdd = new ArrayList<>();
            projectsToAdd.add(project);
            projectsToAdd.add(project);
            List<Project> addProjects = projectRepository.saveAll(projectsToAdd);
            Assertions.assertThat(addProjects.get(0).getName()).isEqualTo(project.getName());
            Assertions.assertThat(addProjects.get(1).getName()).isEqualTo(project.getName());
            logger.info("addAll passed");
        } catch (Throwable t) {
            logger.error("addAll failed", t);
            throw t;
        }
    }

    @Test
    public void findByNameTest() {
        test=extent.createTest("FindByName Project Test");
        logger.info("Starting findByNameTest");
        try {
            List<Project> projects = projectRepository.findAll();
            Project projectToFind = projects.get(0);
            Project project = projectRepository.findByName(projectToFind.getName()).get(0);
            Assertions.assertThat(project.getName()).isEqualTo(projectToFind.getName());
            logger.info("findByNameTest passed");
        } catch (Throwable t) {
            logger.error("findByNameTest failed", t);
            throw t;
        }
    }
    @AfterAll
    public static void tearDown()
    {
        extent.flush();
    }
}
