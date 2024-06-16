package com.ERP.repositoriesTest;

import com.ERP.entities.Client;
import com.ERP.entities.Employee;
import com.ERP.entities.Project;
import com.ERP.entitiesTest.JsonReader;
import com.ERP.repositories.ClientRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;
    Client client;
    Client store;


    JsonReader jsonReader = new JsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Client");

    String name = (String) dataMap.get("name");
    String email = (String) dataMap.get("email");
    String phone = (String) dataMap.get("phone");
    String address = (String) dataMap.get("address");

    ClientRepositoryTest() throws IOException {
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


        store  =  clientRepository.save(client);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void findById()
    {
        Optional<Client> client1 = clientRepository.findById(1L);
        if( client1.isPresent())
            assertThat(client1.get().getName()).isEqualTo(store.getName());

    }

    @Test
    void findAllEmployees()
    {
        List<Client>clientList = clientRepository.findAll();
        assertThat(clientList.get(clientList.size()-1).getName()).isEqualTo(client.getName());
    }

    @Test
    void testdelete()
    {
        client = Client.builder()
                .clientId(1L)
                .name(name)
                .email(email)
                .phone(phone)
                .address(address)
                .projectSet(new HashSet<>())
                .build();

       store = clientRepository.save(client);
        clientRepository.delete(clientRepository.findById(store.getClientId()).get());
        Optional<Client> client1 = clientRepository.findById(store.getClientId());

        Client deletedClient =null;
        if(client1.isPresent())
        {
            deletedClient=client1.get();
        }
        assertThat(deletedClient).isNull();
    }
}