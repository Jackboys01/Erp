package com.ERP.repositories;

import com.ERP.entities.SalaryStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryStructureRepository extends JpaRepository<SalaryStructure, Long> {
    public List<SalaryStructure> findAllByRole(String role);
}
