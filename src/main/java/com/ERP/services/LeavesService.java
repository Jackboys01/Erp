package com.ERP.services;

import com.ERP.entities.Leaves;
import com.ERP.exceptions.IdNotFoundException;
import com.ERP.exceptions.LeavesNotFound;
import com.ERP.repositories.EmployeeRepository;
import com.ERP.repositories.LeavesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class LeavesService {

    @Autowired
    LeavesRepository leavesRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public LeavesService(LeavesRepository leavesRepository, EmployeeRepository employeeRepository) {
        this.leavesRepository =leavesRepository;
        this.employeeRepository = employeeRepository;
    }

    public Leaves addLeaves(Leaves leaves) throws LeavesNotFound {
        try{
            leavesRepository.save(leaves);
            return leaves;
        } catch (Exception e) {
            throw new LeavesNotFound("Error adding the leave: "+ e.getMessage());
        }
    }


    public Leaves updateLeaves(Leaves leaves, Long leaveId) throws LeavesNotFound {
        try {
            // Get the leaves which we need to update
            Leaves leavesToUpdate = leavesRepository.findById(leaveId)
                    .orElseThrow(() -> new IdNotFoundException("Leaves not found with id: " + leaveId));

            // Update leaves fields if they are not null or empty in leaves
            if (Objects.nonNull(leaves.getStartDate())) {
                leavesToUpdate.setStartDate(leaves.getStartDate());
            }
            if (Objects.nonNull(leaves.getEndDate())) {
                leavesToUpdate.setEndDate(leaves.getEndDate());
            }
            if (Objects.nonNull(leaves.getReason()) && !leaves.getReason().isEmpty()) {
                leavesToUpdate.setReason(leaves.getReason());
            }
            if (Objects.nonNull(leaves.getStatus()) && !leaves.getStatus().isEmpty()) {
                leavesToUpdate.setStatus(leaves.getStatus());
            }

            leavesRepository.save(leavesToUpdate);
            return leavesToUpdate;

        }
        catch (IdNotFoundException e){
            throw new IdNotFoundException("Error no id exist: "+ leaveId);
        }
        catch (Exception e) {
            throw new LeavesNotFound("Error updating leaves: " + e.getMessage());
        }
    }


    public Leaves findLeaves(long leaveId) throws LeavesNotFound {
        try{
           return leavesRepository.findById(leaveId).orElseThrow(()->new IdNotFoundException("Leaves not found with id: "+leaveId));
        } catch (IdNotFoundException e) {
            throw new IdNotFoundException(e.getMessage());
        }
        catch (Exception e){
            throw new LeavesNotFound("Error finding the leave: "+ e.getMessage());
        }
    }


    public List<Leaves> findAllLeaves() throws LeavesNotFound {
        try{
            return leavesRepository.findAll();
        } catch (Exception e) {
            throw new LeavesNotFound("Error finding all the leaves: "+e.getMessage());
        }
    }


    public Leaves deleteLeaves(long leaveId) throws LeavesNotFound {
        try{
            Leaves leaves = leavesRepository.findById(leaveId).orElseThrow(()->new IdNotFoundException("Leaves Not found with id: "+leaveId));
            leavesRepository.delete(leaves);
            return leaves;
        }
        catch(IdNotFoundException e){
            throw new IdNotFoundException(e.getMessage());
        }
        catch (Exception e)
        {
            throw new LeavesNotFound("Error deleting the leave: "+e.getMessage());
        }
    }
}
