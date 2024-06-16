package com.ERP.servicesTest;

import com.ERP.entities.Client;
import com.ERP.entities.Employee;
import com.ERP.exceptions.ClientNotFoundException;
import com.ERP.repositories.ClientRepository;
import com.ERP.repositories.EmployeeRepository;
import com.ERP.services.ClientService;
import com.ERP.services.EmployeeService;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.ERP.entitiesTest.JsonReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;
    private ClientService clientService;
    AutoCloseable autoCloseable;
    Client client;



    JsonReader jsonReader = new JsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Client");

    String name = (String) dataMap.get("name");
    String email = (String) dataMap.get("email");
    String phone = (String) dataMap.get("phone");
    String address = (String) dataMap.get("address");

    ClientServiceTest() throws IOException {
    }

    @BeforeEach
    void setUp() throws IOException {
        autoCloseable = MockitoAnnotations.openMocks(this);
        clientService = new ClientService(clientRepository);

        client = Client.builder()
                .clientId(1L)
                .name(name)
                .email(email)
                .phone(phone)
                .address(address)
                .projectSet(new HashSet<>())
                .build();

    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void addClient() throws ClientNotFoundException {
        mock(ClientRepository.class);
        mock(Client.class);
        when(clientRepository.save(client)).thenReturn(client);
        assertThat(clientService.addClient(client)).isEqualTo(client);
    }

    @Test
    void updateClient() throws ClientNotFoundException {
        mock(EmployeeRepository.class);
//        mock(EmployeeDto.class);
        mock(Employee.class);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        // Mocking behavior of employeeRepository.save()
        when(clientRepository.save(client)).thenReturn(client);

        // Assert that the returned EmployeeDto matches the input EmployeeDto

        assertThat(clientService.updateClient(client,1L)).isEqualTo(client);
    }

    @Test
    void findClient() throws ClientNotFoundException {
        mock(ClientRepository.class);
//        mock(EmployeeDto.class);
        mock(Client.class);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        assertThat(clientService.findClient(1L).getName()).isEqualTo(client.getName());

    }

    @Test
    void findAllClients() throws ClientNotFoundException {
        mock(EmployeeRepository.class);
//        mock(EmployeeDto.class);
        mock(Employee.class);

        when(clientRepository.findAll()).thenReturn(new ArrayList<Client>(Collections.singleton(client)));

        assertThat(clientService.findAllClients().get(0)).isEqualTo(client);
    }

    @Test
    void deleteClient() throws ClientNotFoundException {
        mock(ClientRepository.class, Mockito.CALLS_REAL_METHODS);
//        mock(EmployeeDto.class);
        mock(Client.class);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        doAnswer(Answers.CALLS_REAL_METHODS).when(clientRepository).delete(client);
        assertThat(clientService.deleteClient(1L)).isEqualTo(client);
    }
}