package com.ERP.repositories;

import com.ERP.entities.HR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HRRepository extends JpaRepository<HR, Long> {

}
