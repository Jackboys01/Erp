package com.ERP.services;

import com.ERP.dtos.HRDto;
import com.ERP.entities.HR;
import com.ERP.exceptions.IdNotFoundException;
import com.ERP.repositories.HRRepository;
import com.ERP.servicesInter.HRServiceInter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class HRService implements HRServiceInter {
    private final HRRepository hrRepository;
    private final ObjectMapper objectMapper;
    public HRService(HRRepository hrRepository, ObjectMapper objectMapper) {
        this.hrRepository = hrRepository;
        this.objectMapper = objectMapper;
    }

    public HRDto createHR(HRDto hrDto) {
        try {
            HR newHR = objectMapper.convertValue(hrDto, HR.class);
            HR savedHR = hrRepository.save(newHR);
            return objectMapper.convertValue(savedHR, HRDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error adding HR: " + e.getMessage());
        }
    }

    public HRDto updateHR(long hrId, HRDto hrDto) {
        try {
            HR hrToUpdate = hrRepository.findById(hrId)
                    .orElseThrow(() -> new IdNotFoundException("HR not found with id: " + hrId));

            // Update HR fields if they are not null or empty in HRDto
            if (Objects.nonNull(hrDto.getName()) && !hrDto.getName().isEmpty()) {
                hrToUpdate.setName(hrDto.getName());
            }
            if (Objects.nonNull(hrDto.getPassword()) && !hrDto.getPassword().isEmpty()) {
                hrToUpdate.setPassword(hrDto.getPassword());
            }
            if (Objects.nonNull(hrDto.getRole()) && !hrDto.getRole().isEmpty()) {
                hrToUpdate.setRole(hrDto.getRole());
            }

            HR updatedHR = hrRepository.save(hrToUpdate);
            return objectMapper.convertValue(updatedHR, HRDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error updating HR: " + e.getMessage());
        }
    }

    public List<HRDto> getAllHR() {
        try {
            List<HR> hrList = hrRepository.findAll();
            return Arrays.asList(objectMapper.convertValue(hrList, HRDto[].class));
        } catch (Exception e) {
            throw new IdNotFoundException("Error finding all HRs: " + e.getMessage());
        }
    }

    public HRDto getHRById(long hrId) {
        try {
            HR foundHR = hrRepository.findById(hrId)
                    .orElseThrow(() -> new IdNotFoundException("HR not found with id: " + hrId));
            return objectMapper.convertValue(foundHR, HRDto.class);
        } catch (Exception e) {
            System.out.println("Error occured");
            throw new IdNotFoundException("Error finding HR: " + e.getMessage());
        }
    }

    public HRDto deleteHR(long hrId) {
        try {
            HR hrToDelete = hrRepository.findById(hrId)
                    .orElseThrow(() -> new IdNotFoundException("HR not found with id: " + hrId));
            hrRepository.deleteById(hrId);
            return objectMapper.convertValue(hrToDelete, HRDto.class);
        } catch (Exception e) {
            throw new IdNotFoundException("Error deleting HR: " + e.getMessage());
        }
    }
}
