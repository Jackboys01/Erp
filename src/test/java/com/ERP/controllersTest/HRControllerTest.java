package com.ERP.controllersTest;

import com.ERP.controllers.HRController;
import com.ERP.dtos.HRDto;
import com.ERP.services.HRService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class HRControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private HRController hrController;
    @Mock
    private HRService hrService;

    @Test
    void testAddHR_Success() {
        // Prepare test data
        HRDto hrDto = new HRDto(); // Create HRDto object as per your requirement

        // Mock HRService behavior
        when(hrService.createHR(hrDto)).thenReturn(new HRDto()); // Assuming createHR always returns a non-null HRDto

        // Call controller method
        ResponseEntity<Object> responseEntity = hrController.addHR(hrDto);

        // Verify response
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testAddHR_Failure() {
        // Prepare test data
        HRDto hrDto = new HRDto(); // Create HRDto object as per your requirement

        // Mock HRService behavior to return null
        when(hrService.createHR(hrDto)).thenReturn(null);

        // Call controller method
        ResponseEntity<Object> responseEntity = hrController.addHR(hrDto);

        // Verify response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateHR_Success() {
        // Prepare test data
        long hrId = 1;
        HRDto hrDto = new HRDto(); // Create HRDto object as per your requirement

        // Mock HRService behavior
        when(hrService.updateHR(hrId, hrDto)).thenReturn(new HRDto()); // Assuming updateHR always returns a non-null HRDto

        // Call controller method
        ResponseEntity<Object> responseEntity = hrController.updateHR(hrId, hrDto);

        // Verify response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateHR_Failure() {
        // Prepare test data
        long hrId = 1;
        HRDto hrDto = new HRDto(); // Create HRDto object as per your requirement

        // Mock HRService behavior to return null
        when(hrService.updateHR(hrId, hrDto)).thenReturn(null);

        // Call controller method
        ResponseEntity<Object> responseEntity = hrController.updateHR(hrId, hrDto);

        // Verify response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testFindHR_Exists() {
        // Prepare test data
        long hrId = 1;
        HRDto hrDto = new HRDto(); // Create HRDto object as per your requirement

        // Mock HRService behavior
        when(hrService.getHRById(hrId)).thenReturn(hrDto); // Assuming getHRById returns a non-null HRDto when HR exists

        // Call controller method
        ResponseEntity<Object> responseEntity = hrController.findHR(hrId);

        // Verify response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testFindHR_NotFound() {
        // Prepare test data
        long hrId = 1;

        // Mock HRService behavior to return null
        when(hrService.getHRById(hrId)).thenReturn(null); // Assuming getHRById returns null when HR is not found

        // Call controller method
        ResponseEntity<Object> responseEntity = hrController.findHR(hrId);

        // Verify response
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testFindAllHR_ReturnsListOfHRDto() {
        // Prepare test data
        List<HRDto> hrDtoList = List.of(
                new HRDto(1L, "HR 1", "password1", "Admin"),
                new HRDto(2L, "HR 2", "password2", "User")
        );

        // Mock HRService behavior to return the list of HRDto objects
        when(hrService.getAllHR()).thenReturn(hrDtoList);

        // Call controller method
        List<HRDto> actualHrDtoList = hrController.findAllHR();

        // Verify response
        assertEquals(hrDtoList, actualHrDtoList);
    }

    @Test
    void testFindAllHR_NotFound() {
        // Mock HRService behavior to return an empty list
        when(hrService.getAllHR()).thenReturn(Collections.emptyList());

        // Call controller method
        List<HRDto> actualHrDtoList = hrController.findAllHR();

        // Verify response
        assertEquals(Collections.emptyList(), actualHrDtoList);
    }

    @Test
    void testDeleteHR_ReturnsDeletedHRDto() {
        // Prepare test data
        long hrId = 1;
        HRDto deletedHRDto = new HRDto(1L, "HR 1", "password1", "Admin");

        // Mock HRService behavior to return the deleted HRDto object
        when(hrService.deleteHR(hrId)).thenReturn(deletedHRDto);

        // Call controller method
        ResponseEntity<Object> responseEntity = hrController.deleteHR(hrId);

        // Verify response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteHR_NotFound() {
        // Prepare test data
        long hrId = 1;

        // Mock HRService behavior to return null when HR is not found
        when(hrService.deleteHR(hrId)).thenReturn(null);

        // Call controller method
        ResponseEntity<Object> responseEntity = hrController.deleteHR(hrId);

        // Verify response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
