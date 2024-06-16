//package com.ERP.repositoriesTest;
//
//import com.ERP.entities.SalaryStructure;
//import com.ERP.repositories.SalaryStructureRepository;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Use a separate test database
//class SalaryStructureRepositoryTest {
//
//    @Autowired
//    SalaryStructureRepository salaryStructureRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @BeforeEach
//    void setUp() {
//        SalaryStructure salaryStructure = SalaryStructure.builder()
//                .role("developer12")
//                .baseSalary(30000.00)
//                .level("intern")
//                .baseSalary(30000.00).build();
//        entityManager.persist(salaryStructure);
//    }
//
//    @Test
//    @Transactional // Roll back changes after test method execution
//    void findAllByRole() {
//        List<SalaryStructure> salaryStructure = salaryStructureRepository.findAllByRole("developer12");
//        assertEquals(salaryStructure.get(0).getRole(), "developer12");
//    }
//}