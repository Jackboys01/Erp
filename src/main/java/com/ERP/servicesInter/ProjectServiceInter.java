package com.ERP.servicesInter;

import com.ERP.dtos.ProjectDto;

import java.util.List;

public interface ProjectServiceInter
{
     ProjectDto addProject(ProjectDto projectDto);
     ProjectDto updateProject(ProjectDto projectDto,long projectId);
     ProjectDto deleteProject(long projectId);
     ProjectDto findProject(long projectId);
     List<ProjectDto> addAllProject(List<ProjectDto> projectDtos);
     List<ProjectDto> findAllProject();
}
