package com.ERP.controllers;

import com.ERP.entities.Leaves;
import com.ERP.exceptions.IdNotFoundException;
import com.ERP.services.LeavesService;
import com.ERP.utils.MyResponseGenerator;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaves")
public class LeavesController{

    @Autowired
    LeavesService leavesService;

    @PostMapping("/add")
    public ResponseEntity<Object> addLeaves(@Valid @RequestBody Leaves leaves) {
        try {
            Leaves newLeaves = leavesService.addLeaves(leaves);
            return MyResponseGenerator.generateResponse(HttpStatus.CREATED, true, "Leaves are added", newLeaves);
        } catch (Exception e) {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Error adding leaves: " + e.getMessage(), null);
        }
    }

    @PutMapping("/update/{leaveId}")
    public ResponseEntity<Object> updateLeaves(@Valid @RequestBody Leaves leaves, @PathVariable Long leaveId) {
        try {
            Leaves updatedLeaves = leavesService.updateLeaves(leaves, leaveId);
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Leaves are updated successfully", updatedLeaves);
        } catch (Exception e) {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Error updating leaves: " + e.getMessage(), null);
        }
    }

    @GetMapping("/find/{leaveId}")
    public ResponseEntity<Object> findLeaves(@PathVariable long leaveId) {
        try {
            Leaves leavesToFind = leavesService.findLeaves(leaveId);
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Leaves are found", leavesToFind);
        } catch (IdNotFoundException e) {
            return MyResponseGenerator.generateResponse(HttpStatus.NOT_FOUND, false, "Leaves not found with id: " + leaveId, null);
        } catch (Exception e) {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Error finding leaves: " + e.getMessage(), null);
        }
    }

    @GetMapping("/findAll")

    public ResponseEntity<Object> findAllLeaves() {
        try {
            List<Leaves> leavesList = leavesService.findAllLeaves();
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Leaves are found", leavesList);
        } catch (Exception e) {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Error finding leaves: " + e.getMessage(), null);
        }
    }

    @DeleteMapping("/delete/{leaveId}")
    public ResponseEntity<Object> deleteLeaves(@PathVariable long leaveId) {
        try {
            Leaves deletedLeaves = leavesService.deleteLeaves(leaveId);
            return MyResponseGenerator.generateResponse(HttpStatus.OK, true, "Leaves are deleted successfully", deletedLeaves);
        } catch (IdNotFoundException e) {
            return MyResponseGenerator.generateResponse(HttpStatus.NOT_FOUND, false, "Leaves not found with id: " + leaveId, null);
        } catch (Exception e) {
            return MyResponseGenerator.generateResponse(HttpStatus.BAD_REQUEST, false, "Error deleting leaves: " + e.getMessage(), null);
        }
    }

}
