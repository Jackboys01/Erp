package com.ERP.services;

import com.ERP.dtos.ProjectDto;
import com.ERP.entities.Client;
import com.ERP.entities.Project;
import com.ERP.exceptions.ClientNotFoundException;
import com.ERP.exceptions.IdNotFoundException;
import com.ERP.repositories.ClientRepository;
import com.ERP.repositories.ProjectRepository;
import com.ERP.servicesInter.ProjectServiceInter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class ProjectService implements ProjectServiceInter
{
    private ProjectRepository projectRepository;
    private ObjectMapper objectMapper;

    public ProjectService(ProjectRepository projectRepository, ObjectMapper objectMapper)
    {
        this.projectRepository=projectRepository;
        this.objectMapper=objectMapper;
    }

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public ProjectDto addProject(ProjectDto projectDto) {
        try {
            Project newProject = objectMapper.convertValue(projectDto, Project.class);
            projectRepository.save(newProject);
            return objectMapper.convertValue(newProject, ProjectDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error adding project: " + e.getMessage());
        }
    }

    @Override
    public ProjectDto updateProject(ProjectDto projectDto, long projectId) {
        try {
            //get the project which we need to update
            Project project= projectRepository.findById(projectId).orElseThrow(()-> new IdNotFoundException("Project not found with id: "+projectId));

            // Update project fields if they are not null or empty in projectDto
            if (Objects.nonNull(projectDto.getName()) && !projectDto.getName().isEmpty()) {
                project.setName(projectDto.getName());
            }
            if (Objects.nonNull(projectDto.getDescription()) && !projectDto.getDescription().isEmpty()) {
                project.setDescription(projectDto.getDescription());
            }
            if (Objects.nonNull(projectDto.getStartDate())) {
                project.setStartDate(projectDto.getStartDate());
            }
            if (Objects.nonNull(projectDto.getEndDate())) {
                project.setEndDate(projectDto.getEndDate());
            }
            if (Objects.nonNull(projectDto.getStatus()) && !projectDto.getStatus().isEmpty()) {
                project.setStatus(projectDto.getStatus());
            }
            projectRepository.save(project);
            return objectMapper.convertValue(project, ProjectDto.class);
            }
        catch (Exception e) {
            throw new IdNotFoundException("Error updating project: " + e.getMessage());
        }
    }

    @Override
    public ProjectDto deleteProject(long projectId) {
        try {
            Project projectToDelete = projectRepository.findById(projectId)
                    .orElseThrow(() -> new IdNotFoundException("Project not found with id: " + projectId));
            projectRepository.deleteById(projectId);
            return objectMapper.convertValue(projectToDelete, ProjectDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error deleting project: " + e.getMessage());
        }
    }

    @Override
    public ProjectDto findProject(long projectId) {
        try {
            Project projectToSearch = projectRepository.findById(projectId)
                    .orElseThrow(() -> new IdNotFoundException("Project not found with id: " + projectId));
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.convertValue(projectToSearch, ProjectDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error finding project: " + e.getMessage());
        }
    }

    @Override
    public List<ProjectDto> addAllProject(List<ProjectDto> projectDtos) {
        try {
            List<Project> projectList = Arrays.asList(objectMapper.convertValue(projectDtos, Project[].class));
            projectRepository.saveAll(projectList);
            return Arrays.asList(objectMapper.convertValue(projectDtos, ProjectDto[].class));
        } catch (Exception e) {
            throw new IdNotFoundException("Error adding all projects: " + e.getMessage());
        }
    }

    @Override
    public List<ProjectDto> findAllProject() {
        try {
            List<Project> projectList = projectRepository.findAll();
            return Arrays.asList(objectMapper.convertValue(projectList, ProjectDto[].class));
        } catch (Exception e) {
            throw new IdNotFoundException("Error finding all projects: " + e.getMessage());
        }
    }

    public Project addProjectToClient(Long clientId, Project project) throws ClientNotFoundException {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with id: " + clientId));
        project.setClient(client);
        return projectRepository.save(project);
    }

    public List<Project> getProjectsByClient(Long clientId) throws ClientNotFoundException {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with id: " + clientId));
        return projectRepository.findByClient(client);

    }

    public Project updateProjectStatus(Long clientId, Long projectId, String status) throws ClientNotFoundException {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with id: " + clientId));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IdNotFoundException("Project not found with id: " + projectId));
        if (!project.getClient().equals(client)) {
            throw new IllegalArgumentException("Project does not belong to the client");
        }
        project.setStatus(status);
        return projectRepository.save(project);
    }
}
