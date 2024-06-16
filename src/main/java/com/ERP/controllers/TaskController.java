package com.ERP.controllers;

import com.ERP.dtos.TaskDto;
import com.ERP.services.TaskService;
import com.ERP.utils.MyResponseGenerator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addTask(@Valid @RequestBody TaskDto taskDTO) {
        TaskDto createdTask = taskService.createTask(taskDTO);
        if (createdTask != null) {
            return MyResponseGenerator.generateResponse(HttpStatus.CREATED, true, "Task added successfully", createdTask);
        } else {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Failed to add task", null);
        }
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<Object> updateTask(@PathVariable Long taskId, @Valid @RequestBody TaskDto taskDTO) {
        TaskDto updatedTask = taskService.updateTask(taskId, taskDTO);
        if (updatedTask != null) {
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Task updated successfully", updatedTask);
        } else {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Failed to update task", null);
        }
    }

    @GetMapping("/find/{taskId}")
    public ResponseEntity<Object> findTask(@PathVariable Long taskId) {
        TaskDto foundTask = taskService.getTaskById(taskId);
        if (foundTask != null) {
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Task found", foundTask);
        } else {
            return MyResponseGenerator.generateResponse(HttpStatus.NOT_FOUND, false, "Task not found", null);
        }
    }

    @GetMapping("/findAll")
    public List<TaskDto> findAllTasks() {
        return taskService.getAllTask();
    }

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<Object> deleteTask(@PathVariable Long taskId) {
        TaskDto deletedTask = taskService.deleteTask(taskId);
        if (deletedTask != null) {
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Task deleted successfully", deletedTask);
        } else {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Failed to delete task", null);
        }
    }
}
