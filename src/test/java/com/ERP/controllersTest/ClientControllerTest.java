package com.ERP.controllersTest;

import com.ERP.config.TestSecurityConfig;
import com.ERP.controllers.ClientController;
import com.ERP.controllers.EmployeeController;
import com.ERP.entities.Client;
import com.ERP.entitiesTest.JsonReader;
import com.ERP.exceptions.ClientNotFoundException;
import com.ERP.security.JwtHelper;
import com.ERP.services.ClientService;
import com.ERP.services.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = TestSecurityConfig.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtHelper jwtHelper;

    @MockBean
    private ClientService clientService;

    Client client;

    List<Client>clientList = new ArrayList<>();
    JsonReader jsonReader = new JsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Client");

    String name = (String) dataMap.get("name");
    String email = (String) dataMap.get("email");
    String phone = (String) dataMap.get("phone");
    String address = (String) dataMap.get("address");

    ClientControllerTest() throws IOException {
    }


    @BeforeEach
    void setUp() {
        client = Client.builder()
                .clientId(1L)
                .name(name)
                .email(email)
                .phone(phone)
                .address(address)
                .projectSet(new HashSet<>())
                .build();

        clientList.add(client);
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
    void addClient() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(client);

        when(clientService.addClient(client)).thenReturn(client);
        this.mockMvc.perform(post("/client/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print()).andExpect(status().isCreated());

    }

    @Test
    @WithMockUser
    void updateClient() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(client);

        when(clientService.updateClient(client, 1L)).thenReturn(client);
        this.mockMvc.perform(put("/client/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void findClient() throws Exception {

        when(clientService.findClient(1L)).thenReturn(client);
        this.mockMvc.perform(get("/client/find/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Client Name"));
    }

    @Test
    @WithMockUser
    void findAllClients() throws Exception {
        when(clientService.findAllClients()).thenReturn(clientList);
        this.mockMvc.perform(get("/client/findAll")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void deleteClient() throws Exception {
        when(clientService.deleteClient(anyLong())).thenReturn(client);
        this.mockMvc.perform(delete("/client/delete/" + "1")).andDo(print()).andExpect(status().isOk());
    }
}