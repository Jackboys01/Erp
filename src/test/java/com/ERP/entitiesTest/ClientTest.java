package com.ERP.entitiesTest;

import com.ERP.entities.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ClientTest {
    private static final Logger logger = LoggerFactory.getLogger(ClientTest.class);
    private Client client;

    ClientJsonReader clientJsonReader;
    Map<String, Object> dataMap;

    String name;
    String email;
    String phone;
    String address;

    public ClientTest() throws IOException {
        clientJsonReader = new ClientJsonReader();
        dataMap = clientJsonReader.readFile("Client"); // Ensure the JSON file name is "Client.json" under "src/test/resources/payloads/"
        name = (String) dataMap.get("name");
        email = (String) dataMap.get("email");
        phone = (String) dataMap.get("phone");
        address = (String) dataMap.get("address");
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = Client.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .address(address)
                .build();
    }

    @Test
    void testConstructor() {
        logger.info("Testing constructor");
        assertEquals(name, client.getName());
        assertEquals(email, client.getEmail());
        assertEquals(phone, client.getPhone());
        assertEquals(address, client.getAddress());
    }

    @Test
    void testGetters() {
        logger.info("Testing getters");
        assertEquals(name, client.getName());
        assertEquals(email, client.getEmail());
        assertEquals(phone, client.getPhone());
        assertEquals(address, client.getAddress());
    }

    @Test
    void testSetters() {
        logger.info("Testing setters");
        client.setName("Updated Client");
        client.setEmail("updated@example.com");
        client.setPhone("+0987654321");
        client.setAddress("Updated Address");
        assertEquals("Updated Client", client.getName());
        assertEquals("updated@example.com", client.getEmail());
        assertEquals("+0987654321", client.getPhone());
        assertEquals("Updated Address", client.getAddress());
    }
}
