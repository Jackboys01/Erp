package com.ERP.repositories;

import com.ERP.entities.Client;
import com.ERP.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long>
{
     List<Project> findByName(String username);

    List<Project> findByClient(Client client);
}
