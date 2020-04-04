package com.shiffler.AcmeTestingCenter.repository;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface MedicalTestRepository extends CrudRepository<MedicalTest, UUID> {

    Optional<MedicalTest> findByTestCode(String testCode);
    Iterable<MedicalTest> findByTestNameContaining(String testName);

    @Transactional
    @Query("select m from MedicalTest m where m.minOnHand > m.quantityOnHand")
    List<MedicalTest> findByTestLessThanMinOnHand();

}
