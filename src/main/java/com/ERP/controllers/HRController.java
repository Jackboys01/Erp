package com.ERP.controllers;

import com.ERP.dtos.HRDto;
import com.ERP.services.HRService;
import com.ERP.utils.MyResponseGenerator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hr")
public class HRController {

    @Autowired
    private final HRService hrService;

    @Autowired
    public HRController(HRService hrService) {
        this.hrService = hrService;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addHR(@Valid @RequestBody HRDto hrDto) {
        HRDto createdHR = hrService.createHR(hrDto);
        if (createdHR != null) {
            return MyResponseGenerator.generateResponse(HttpStatus.CREATED, true, "HR added successfully", createdHR);
        } else {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Failed to add HR", null);
        }
    }

    @PutMapping("/update/{hrId}")
    public ResponseEntity<Object> updateHR(@PathVariable long hrId, @Valid @RequestBody HRDto hrDto) {
        HRDto updatedHR = hrService.updateHR(hrId, hrDto);
        if (updatedHR != null) {
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "HR updated successfully", updatedHR);
        } else {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Failed to update HR", null);
        }
    }

    @GetMapping("/find/{hrId}")
    public ResponseEntity<Object> findHR(@PathVariable long hrId) {
        HRDto foundHR = hrService.getHRById(hrId);
        if (foundHR != null) {
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "HR found", foundHR);
        } else {
            return MyResponseGenerator.generateResponse(HttpStatus.NOT_FOUND, false, "HR not found", null);
        }
    }

    @GetMapping("/findAll")
    public List<HRDto> findAllHR() {
        return hrService.getAllHR();
    }

    @DeleteMapping("/delete/{hrId}")
    public ResponseEntity<Object> deleteHR(@PathVariable long hrId) {
        HRDto deletedHR = hrService.deleteHR(hrId);
        if (deletedHR != null) {
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "HR deleted successfully", deletedHR);
        } else {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Failed to delete HR", null);
        }
    }
}
