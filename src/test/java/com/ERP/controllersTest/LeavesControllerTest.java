package com.ERP.controllersTest;

import com.ERP.config.TestSecurityConfig;
import com.ERP.controllers.ClientController;
import com.ERP.controllers.LeavesController;
import com.ERP.entities.Employee;
import com.ERP.entities.Leaves;
import com.ERP.entitiesTest.JsonReader;
import com.ERP.security.JwtHelper;
import com.ERP.services.LeavesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.AfterEach;
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

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LeavesController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = TestSecurityConfig.class)
class LeavesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeavesService leavesService;
    @MockBean
    private JwtHelper jwtHelper;

    Leaves leaves;

    List<Leaves> leavesList = new ArrayList<>();



    JsonReader jsonReader = new JsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Leaves");



    String startDateString = (String) dataMap.get("startDate");
    String endDateString = (String) dataMap.get("endDate");
    Date startDate = Date.valueOf(startDateString);
    Date endDate = Date.valueOf(endDateString);
    String reason = (String) dataMap.get("reason");
    String status = (String) dataMap.get("status");
    Long employeeId = Long.parseLong(((Map<String, Object>) dataMap.get("employee")).get("id").toString());

    LeavesControllerTest() throws IOException {
    }


    @BeforeEach
    void setUp() {
        Employee employee = new Employee();
        employee.setId(employeeId); // Set the ID to 1
        employee.setName("Name"); // Set the name field



        leaves = Leaves.builder()
                .id(1L)
                .startDate(startDate)
                .endDate(endDate)
                .reason(reason)
                .status(status)
                .employee(employee)
                .build();

        leavesList.add(leaves);
        // Setup mock JWT token
        when(jwtHelper.generateToken(Mockito.any())).thenReturn("mock-token");

        // Setup Security context
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @WithMockUser
    void addLeaves() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(leaves);

        when(leavesService.addLeaves(leaves)).thenReturn(leaves);
        this.mockMvc.perform(post("/leaves/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print()).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void updateLeaves() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(leaves);

        when(leavesService.updateLeaves(leaves, 1L)).thenReturn(leaves);
        this.mockMvc.perform(put("/leaves/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void findLeaves() throws Exception {
        when(leavesService.findLeaves(1L)).thenReturn(leaves);
        this.mockMvc.perform(get("/leaves/find/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.reason").value(reason));
    }

    @Test
    @WithMockUser
    void findAllLeaves() throws Exception {
        when(leavesService.findAllLeaves()).thenReturn(leavesList);
        this.mockMvc.perform(get("/leaves/findAll")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void deleteLeaves() throws Exception {
        when(leavesService.deleteLeaves(anyLong())).thenReturn(leaves);
        this.mockMvc.perform(delete("/leaves/delete/" + "1")).andDo(print()).andExpect(status().isOk());
    }
}