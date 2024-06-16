package com.ERP.servicesTest;

import com.ERP.dtos.TaskDto;
import com.ERP.entities.Task;
import com.ERP.exceptions.IdNotFoundException;
import com.ERP.repositories.TaskRepository;
import com.ERP.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_ValidTaskDto_ReturnsTaskDto() {
        TaskDto taskDto = new TaskDto(1L, "Task 1", "Description", Date.valueOf("2024-05-13"), Date.valueOf("2024-05-14"), "Pending");
        Task task = new Task(1L, "Task 1", "Description", Date.valueOf("2024-05-13"), Date.valueOf("2024-05-14"), "Pending", null, null);

        when(objectMapper.convertValue(taskDto, Task.class)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(objectMapper.convertValue(task, TaskDto.class)).thenReturn(taskDto);

        TaskDto createdTaskDto = taskService.createTask(taskDto);

        assertNotNull(createdTaskDto);
        assertEquals(taskDto, createdTaskDto);

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void updateTask_ExistingTaskIdAndValidTaskDto_ReturnsUpdatedTaskDto() {
        long taskId = 1L;
        TaskDto taskDto = new TaskDto(taskId, "Updated Task 1", "Updated Description", Date.valueOf("2024-05-14"), Date.valueOf("2024-05-15"), "In Progress");
        Task existingTask = new Task(taskId, "Task 1", "Description", Date.valueOf("2024-05-13"), Date.valueOf("2024-05-14"), "Pending", null, null);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);
        when(objectMapper.convertValue(existingTask, TaskDto.class)).thenReturn(taskDto);

        TaskDto updatedTaskDto = taskService.updateTask(taskId, taskDto);

        assertNotNull(updatedTaskDto);
        assertEquals(taskDto, updatedTaskDto);

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void updateTask_NonExistingTaskId_ThrowsIdNotFoundException() {
        long taskId = 1L;
        TaskDto taskDto = new TaskDto(taskId, "Updated Task 1", "Updated Description", Date.valueOf("2024-05-14"), Date.valueOf("2024-05-15"), "In Progress");

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> taskService.updateTask(taskId, taskDto));

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void getAllTask_ReturnsListOfTaskDto() {
        List<Task> taskList = Arrays.asList(
                new Task(1L, "Task 1", "Description 1", Date.valueOf("2024-05-13"), Date.valueOf("2024-05-14"), "Pending", null, null),
                new Task(2L, "Task 2", "Description 2", Date.valueOf("2024-05-14"), Date.valueOf("2024-05-15"), "In Progress", null, null)
        );

        TaskDto taskDto1 = new TaskDto(1L, "Task 1", "Description 1", Date.valueOf("2024-05-13"), Date.valueOf("2024-05-14"), "Pending");
        TaskDto taskDto2 = new TaskDto(2L, "Task 2", "Description 2", Date.valueOf("2024-05-14"), Date.valueOf("2024-05-15"), "In Progress");

        when(taskRepository.findAll()).thenReturn(taskList);
        when(objectMapper.convertValue(any(Task.class), eq(TaskDto.class)))
                .thenReturn(taskDto1)
                .thenReturn(taskDto2);

        List<TaskDto> allTaskDto = taskService.getAllTask();

        assertNotNull(allTaskDto);
        assertEquals(2, allTaskDto.size());

        verify(taskRepository, times(1)).findAll();
        verify(objectMapper, times(2)).convertValue(any(Task.class), eq(TaskDto.class));
    }

    @Test
    void getTaskById_ExistingTaskId_ReturnsTaskDto() {
        long taskId = 1L;
        Task task = new Task(taskId, "Task 1", "Description", Date.valueOf("2024-05-13"), Date.valueOf("2024-05-14"), "Pending", null, null);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(objectMapper.convertValue(task, TaskDto.class)).thenReturn(new TaskDto(taskId, "Task 1", "Description", Date.valueOf("2024-05-13"), Date.valueOf("2024-05-14"), "Pending"));

        TaskDto taskDto = taskService.getTaskById(taskId);

        assertNotNull(taskDto);
        assertEquals(taskId, taskDto.getTaskId());

        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void getTaskById_NonExistingTaskId_ThrowsIdNotFoundException() {
        long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> taskService.getTaskById(taskId));

        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void deleteTask_ExistingTaskId_ReturnsDeletedTaskDto() {
        long taskId = 1L;
        Task task = new Task(taskId, "Task 1", "Description", Date.valueOf("2024-05-13"), Date.valueOf("2024-05-14"), "Pending", null, null);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).deleteById(taskId);
        when(objectMapper.convertValue(task, TaskDto.class)).thenReturn(new TaskDto(taskId, "Task 1", "Description", Date.valueOf("2024-05-13"), Date.valueOf("2024-05-14"), "Pending"));

        TaskDto deletedTaskDto = taskService.deleteTask(taskId);

        assertNotNull(deletedTaskDto);
        assertEquals(taskId, deletedTaskDto.getTaskId());

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    void deleteTask_NonExistingTaskId_ThrowsIdNotFoundException() {
        long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> taskService.deleteTask(taskId));

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).deleteById(anyLong());
    }
}