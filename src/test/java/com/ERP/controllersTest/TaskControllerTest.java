package com.ERP.controllersTest;

import com.ERP.config.TestSecurityConfig;
import com.ERP.controllers.ClientController;
import com.ERP.controllers.TaskController;
import com.ERP.dtos.TaskDto;
import com.ERP.security.JwtHelper;
import com.ERP.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = TestSecurityConfig.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;
    @MockBean
    private JwtHelper jwtHelper;

    @BeforeEach
    void setUp()
    {
        when(jwtHelper.generateToken(Mockito.any())).thenReturn("mock-token");

        // Setup Security context
        SecurityContextHolder.clearContext();
    }

    @Test
    @WithMockUser
    void getTask() throws Exception {
        // Mock data
        TaskDto taskDto1 = new TaskDto();
        TaskDto taskDto2 = new TaskDto();
        List<TaskDto> taskDtoList = Arrays.asList(taskDto1, taskDto2);

        // Mock service method
        when(taskService.getAllTask()).thenReturn(taskDtoList);

        // Perform GET request
        mockMvc.perform(get("/task/findAll"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser
    void addTask() throws Exception {
        TaskDto taskDto = TaskDto.builder()
                .taskId(1L)
                .name("complete task entity")
                .description("Create entity Dto service repository and unit testing of all the methods.")
                .startDate(Date.valueOf("2024-05-13"))
                .endDate(Date.valueOf("2024-05-13"))
                .status("Assigned")
                .build();

        when(taskService.createTask(any(TaskDto.class))).thenReturn(taskDto);

        mockMvc.perform(post("/task/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void getTaskById() throws Exception {
        TaskDto taskDto = TaskDto.builder()
                .taskId(1L)
                .name("complete task entity")
                .description("Create entity Dto service repository and unit testing of all the methods.")
                .startDate(Date.valueOf("2024-05-13"))
                .endDate(Date.valueOf("2024-05-13"))
                .status("Assigned")
                .build();

        when(taskService.getTaskById(1L)).thenReturn(taskDto);

        mockMvc.perform(get("/task/find/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("Assigned"));
    }

    @Test
    @WithMockUser
    void removeTask() throws Exception {
        TaskDto taskDto = TaskDto.builder()
                .taskId(1L)
                .name("complete task entity")
                .description("Create entity Dto service repository and unit testing of all the methods.")
                .startDate(Date.valueOf("2024-05-13"))
                .endDate(Date.valueOf("2024-05-13"))
                .status("Assigned")
                .build();

        when(taskService.deleteTask(1L)).thenReturn(taskDto);
        mockMvc.perform(delete("/task/delete/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void updateTask() throws Exception {
        // Prepare the salary structure object with updated values
        TaskDto existedTaskDto = TaskDto.builder()
                .taskId(1L)
                .name("complete task entity")
                .description("Create entity Dto service repository and unit testing of all the methods.")
                .startDate(Date.valueOf("2024-05-13"))
                .endDate(Date.valueOf("2024-05-13"))
                .status("Assigned")
                .build();

        TaskDto updatedTaskDto = TaskDto.builder()
                .taskId(1L)
                .name("complete task entity")
                .description("Create entity Dto service repository and unit testing of all the methods.")
                .startDate(Date.valueOf("2024-05-13"))
                .endDate(Date.valueOf("2024-05-13"))
                .status("InProgress")
                .build();

        // Mock the service behavior to update the salary structure
        when(taskService.updateTask(eq(1L), any(TaskDto.class)))
                .thenReturn(updatedTaskDto);

        // Perform PUT request to update the salary structure
        mockMvc.perform(put("/task/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedTaskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("InProgress"));
    }
}
