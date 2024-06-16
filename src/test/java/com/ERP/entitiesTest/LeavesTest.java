package com.ERP.entitiesTest;

import com.ERP.entities.Employee;
import com.ERP.entities.Leaves;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class LeavesTest {
    private static final Logger logger = LoggerFactory.getLogger(LeavesTest.class);
    private Leaves leaves;

    JsonReader jsonReader = new JsonReader();
    Map<String, Object> dataMap;

    Date startDate;
    Date endDate;
    String reason;
    String status;
    Employee employee;

    public LeavesTest() throws IOException {
        try {
            dataMap = jsonReader.readFile("Leaves"); // Ensure the JSON file name is "Leaves.json" under "src/test/resources/payloads/"
            startDate = Date.valueOf((String) dataMap.get("startDate"));
            endDate = Date.valueOf((String) dataMap.get("endDate"));
            reason = (String) dataMap.get("reason");
            status = (String) dataMap.get("status");

            Map<String, Object> employeeMap = (Map<String, Object>) dataMap.get("employee");
            employee = Employee.builder().id(((Number) employeeMap.get("id")).longValue()).build();
        } catch (IllegalArgumentException e) {
            logger.error("Error reading JSON file", e);
            throw new IOException("Failed to read JSON file", e);
        }
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        leaves = Leaves.builder()
                .startDate(startDate)
                .endDate(endDate)
                .reason(reason)
                .status(status)
                .employee(employee)
                .build();
    }

    @Test
    void testConstructor() {
        logger.info("Testing constructor");
        assertEquals(startDate, leaves.getStartDate());
        assertEquals(endDate, leaves.getEndDate());
        assertEquals(reason, leaves.getReason());
        assertEquals(status, leaves.getStatus());
        assertEquals(employee, leaves.getEmployee());
    }

    @Test
    void testGetters() {
        logger.info("Testing getters");
        assertEquals(startDate, leaves.getStartDate());
        assertEquals(endDate, leaves.getEndDate());
        assertEquals(reason, leaves.getReason());
        assertEquals(status, leaves.getStatus());
        assertEquals(employee, leaves.getEmployee());
    }

    @Test
    void testSetters() {
        logger.info("Testing setters");
        leaves.setStartDate(Date.valueOf("2024-06-01"));
        leaves.setEndDate(Date.valueOf("2024-06-10"));
        leaves.setReason("Medical Leave");
        leaves.setStatus("Pending");
        Employee newEmployee = Employee.builder().id(3L).build();
        leaves.setEmployee(newEmployee);
        assertEquals(Date.valueOf("2024-06-01"), leaves.getStartDate());
        assertEquals(Date.valueOf("2024-06-10"), leaves.getEndDate());
        assertEquals("Medical Leave", leaves.getReason());
        assertEquals("Pending", leaves.getStatus());
        assertEquals(newEmployee, leaves.getEmployee());
    }
}
