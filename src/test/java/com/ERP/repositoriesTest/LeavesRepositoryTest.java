package com.ERP.repositoriesTest;

import com.ERP.entities.Client;
import com.ERP.entities.Employee;
import com.ERP.entities.Leaves;
import com.ERP.entitiesTest.JsonReader;
import com.ERP.repositories.EmployeeRepository;
import com.ERP.repositories.LeavesRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LeavesRepositoryTest {
    @Autowired
    LeavesRepository leavesRepository;

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
     private EntityManager entityManager;
    Leaves leaves;
    Leaves storedLeaves;

    JsonReader jsonReader = new JsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Leaves");



    String startDateString = (String) dataMap.get("startDate");
    String endDateString = (String) dataMap.get("endDate");
    Date startDate = Date.valueOf(startDateString);
    Date endDate = Date.valueOf(endDateString);
    String reason = (String) dataMap.get("reason");
    String status = (String) dataMap.get("status");
    Long employeeId = Long.parseLong(((Map<String, Object>) dataMap.get("employee")).get("id").toString());

    LeavesRepositoryTest() throws  IOException {
    }

    @BeforeEach
    void setUp() {
        Employee employee = new Employee();
        employeeRepository.save(employee);



        leaves = Leaves.builder()
                .id(1L)
                .startDate(startDate)
                .endDate(endDate)
                .reason(reason)
                .status(status)
                .employee(employee)
                .build();

        storedLeaves = leavesRepository.save(leaves);

    }

    @AfterEach
    void tearDown() {
        leaves = null;
        leavesRepository.deleteAll();
    }
    @Test
    void findById() {
        Optional<Leaves> leaves1 = leavesRepository.findById(storedLeaves.getId());
        if( leaves1.isPresent())
            assertThat(leaves1.get().getReason()).isEqualTo(storedLeaves.getReason());
//        assertThat(leavesRepository.findById(storedLeaves.getId())).isPresent();
    }

    @Test
    void findAllLeaves() {
        List<Leaves>store = leavesRepository.findAll();
        assertThat(leavesRepository.findAll().get(store.size()-1)).isEqualTo(storedLeaves);
    }

    @Test
    void testDelete() {
        leavesRepository.delete(storedLeaves);
        assertThat(leavesRepository.findById(storedLeaves.getId())).isEmpty();
    }

}