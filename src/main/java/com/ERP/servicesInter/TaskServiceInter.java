package com.ERP.servicesInter;

import com.ERP.dtos.TaskDto;

import java.util.List;

public interface TaskServiceInter {
    TaskDto createTask(TaskDto taskDto);

    TaskDto getTaskById(long taskId);

    List<TaskDto> getAllTask();
    TaskDto deleteTask(long taskId);

    TaskDto updateTask(long taskId, TaskDto task);
}
