package com.ERP.servicesTest;

import com.ERP.entities.Employee;
import com.ERP.entities.Leaves;
import com.ERP.entitiesTest.JsonReader;
import com.ERP.exceptions.LeavesNotFound;
import com.ERP.repositories.EmployeeRepository;
import com.ERP.repositories.LeavesRepository;
import com.ERP.services.LeavesService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LeavesServiceTest {

    @Mock
    private LeavesRepository leavesRepository;
    @Mock
    private EmployeeRepository employeeRepository;

    private LeavesService leavesService;
    AutoCloseable autoCloseable;
    Leaves leaves;


    JsonReader jsonReader = new JsonReader();
    Map<String, Object> dataMap = jsonReader.readFile("Leaves");



    String startDateString = (String) dataMap.get("startDate");
    String endDateString = (String) dataMap.get("endDate");
    Date startDate = Date.valueOf(startDateString);
    Date endDate = Date.valueOf(endDateString);
    String reason = (String) dataMap.get("reason");
    String status = (String) dataMap.get("status");
    Long employeeId = Long.parseLong(((Map<String, Object>) dataMap.get("employee")).get("id").toString());

    LeavesServiceTest() throws IOException {
    }


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        leavesService = new LeavesService(leavesRepository, employeeRepository);

        Employee employee = new Employee();
        employee.setId(employeeId); // Set the ID to 1

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);


        leaves = Leaves.builder()
                .id(1L)
                .startDate(startDate)
                .endDate(endDate)
                .reason(reason)
                .status(status)
                .employee(employee)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void addLeaves() throws LeavesNotFound {
        mock(LeavesRepository.class);
        mock(Leaves.class);

        when(leavesRepository.save(leaves)).thenReturn(leaves);
        assertThat(leavesService.addLeaves(leaves)).isEqualTo(leaves);
    }

    @Test
    void updateLeaves() throws LeavesNotFound {
        mock(EmployeeRepository.class);
//        mock(EmployeeDto.class);
        mock(Employee.class);

        when(leavesRepository.findById(1L)).thenReturn(Optional.of(leaves));
        // Mocking behavior of employeeRepository.save()
        when(leavesRepository.save(leaves)).thenReturn(leaves);

        // Assert that the returned EmployeeDto matches the input EmployeeDto

        assertThat(leavesService.updateLeaves(leaves,1L)).isEqualTo(leaves);

    }

    @Test
    void findLeaves() throws LeavesNotFound {
        mock(LeavesRepository.class);
//        mock(EmployeeDto.class);
        mock(Leaves.class);
        when(leavesRepository.findById(1L)).thenReturn(Optional.of(leaves));
        assertThat(leavesService.findLeaves(1L).getReason()).isEqualTo(leaves.getReason());

    }

    @Test
    void findAllLeaves() throws LeavesNotFound {
        mock(LeavesRepository.class);
//        mock(EmployeeDto.class);
        mock(Leaves.class);

        when(leavesRepository.findAll()).thenReturn(new ArrayList<Leaves>(Collections.singleton(leaves)));

        assertThat(leavesService.findAllLeaves().get(0)).isEqualTo(leaves);
    }

    @Test
    void deleteLeaves() throws LeavesNotFound {
        mock(LeavesRepository.class, Mockito.CALLS_REAL_METHODS);
//        mock(EmployeeDto.class);
        mock(Leaves.class);
        when(leavesRepository.findById(1L)).thenReturn(Optional.of(leaves));
        doAnswer(Answers.CALLS_REAL_METHODS).when(leavesRepository).delete(leaves);
        assertThat(leavesService.deleteLeaves(1L)).isEqualTo(leaves);
    }
}