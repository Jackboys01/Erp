package com.ERP.repositories;

import com.ERP.entities.JwtAuthentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends JpaRepository<JwtAuthentication,Long> {

    public JwtAuthentication findByUsername(String username);
}
