package com.shiffler.AcmeTestingCenter.repository;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicalTestRepository extends CrudRepository<MedicalTest, UUID> {

    /**
     * Allows a Medical Test to be found by Test Code. Requires an exact match
     * @param testCode - The testcode to search on
     * @return - An Optional of a MedicalTest
     */
    Optional<MedicalTest> findByTestCode(String testCode);

    /**
     * Returns all of the MedicalTests with a testname that contains a certain String.
     * @param testName
     * @return
     */
    Iterable<MedicalTest> findByTestNameContaining(String testName);

    /**
     * Finds Medical Tests that have less inventory on hand than the configured minimum
     * @return
     */
    //@Transactional //may not need this
    @Query("select m from MedicalTest m where m.minOnHand > m.quantityOnHand")
    List<MedicalTest> findByTestLessThanMinOnHand();


}
