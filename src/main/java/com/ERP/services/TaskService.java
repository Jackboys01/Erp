package com.ERP.services;

import com.ERP.dtos.TaskDto;
import com.ERP.entities.Task;
import com.ERP.exceptions.IdNotFoundException;
import com.ERP.repositories.TaskRepository;
import com.ERP.servicesInter.TaskServiceInter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TaskService implements TaskServiceInter {
    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper;

    public TaskService(TaskRepository taskRepository, ObjectMapper objectMapper) {
        this.taskRepository = taskRepository;
        this.objectMapper = objectMapper;
    }

    public TaskDto createTask(TaskDto taskDTO) {
        try {
            Task newTask = objectMapper.convertValue(taskDTO, Task.class);
            Task savedTask = taskRepository.save(newTask);
            return objectMapper.convertValue(savedTask, TaskDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error adding task: " + e.getMessage());
        }
    }

    public TaskDto updateTask(long taskId, TaskDto taskDTO) {
        try {
            Task taskToUpdate = taskRepository.findById(taskId)
                    .orElseThrow(() -> new IdNotFoundException("Task not found with id: " + taskId));

            // Update task fields if they are not null or empty in taskDTO
            if (Objects.nonNull(taskDTO.getName()) && !taskDTO.getName().isEmpty()) {
                taskToUpdate.setName(taskDTO.getName());
            }
            // Similarly update other fields as needed

            Task updatedTask = taskRepository.save(taskToUpdate);
            return objectMapper.convertValue(updatedTask, TaskDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error updating task: " + e.getMessage());
        }
    }


    @Override
    public List<TaskDto> getAllTask() {
        try {
            List<Task> taskList = taskRepository.findAll();
            List<TaskDto> taskDtoList = new ArrayList<>();
            for (Task task : taskList) {
                taskDtoList.add(objectMapper.convertValue(task, TaskDto.class));
            }
            return taskDtoList;
        } catch (Exception e) {
            throw new IdNotFoundException("Error finding all tasks: " + e.getMessage());
        }
    }
//    @Override
//    public List<TaskDto> getAllTask() {
//        try {
//            List<Task> taskList = taskRepository.findAll();
//            return Arrays.asList(objectMapper.convertValue(taskList, TaskDto[].class));
//        } catch (Exception e) {
//            throw new IdNotFoundException("Error finding all tasks: " + e.getMessage());
//        }
//    }

    public TaskDto getTaskById(long taskId) {
        try {
            Task foundTask = taskRepository.findById(taskId)
                    .orElseThrow(() -> new IdNotFoundException("Task not found with id: " + taskId));
            return objectMapper.convertValue(foundTask, TaskDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error finding task: " + e.getMessage());
        }
    }

    public TaskDto deleteTask(long taskId) {
        try {
            Task taskToDelete = taskRepository.findById(taskId)
                    .orElseThrow(() -> new IdNotFoundException("Task not found with id: " + taskId));
            taskRepository.deleteById(taskId);
            return objectMapper.convertValue(taskToDelete, TaskDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error deleting task: " + e.getMessage());
        }
    }
}
