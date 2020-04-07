/*
Implements all of the functionality available for the different MedicalTest Types.
 */

package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.repository.MedicalTestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class MedicalTestServiceImpl implements MedicalTestService {

    private final MedicalTestRepository medicalTestRepository;

    @Autowired
    MedicalTestServiceImpl(MedicalTestRepository medicalTestRepository){
        this.medicalTestRepository = medicalTestRepository;
    }


    /**
     * Allows a specific type of Medical Test to be retrieved by its id
     * param id - The id of the test we're searching for.
     * @return - An Optional of a MedicalTest
     */
    @Override
    public Optional<MedicalTest> getMedicalTestById(UUID id) {

        return medicalTestRepository.findById(id);
    }

    /**
     * Allows a specific type of Medical Test to be retrieved by its testCode
     * param id - The testCode of the test we're searching for.
     * @return - An Optional of a MedicalTest
     */
    @Override
    public Optional<MedicalTest> getMedicalTestByTestCode(String testCode) {

        return medicalTestRepository.findByTestCode(testCode);
    }

    /**
     * Allows a new Medical Test to be saved.
     * @param medicalTest
     */
    @Override
    public void saveMedicalTest(MedicalTest medicalTest) {

        initializeMedicalTest(medicalTest);
        medicalTestRepository.save(medicalTest);
    }

    /**
     * Retrieves all of the Medical Tests that can be ordered.
     * @return An Iterable of all available MedicalTests
     */
    @Override
    public Iterable<MedicalTest> getAllMedicalTests() {
        return medicalTestRepository.findAll();
    }

    /**
     * Sets initial values if certain fields of the MedicalTest haven't been set
     * @param medicalTest - The MedicalTest to initialize
     */
    private void initializeMedicalTest(MedicalTest medicalTest){

        if (medicalTest.getQuantityToOrder() == null) {
            medicalTest.setQuantityToOrder(10);
        }

        if (medicalTest.getQuantityOnHand() == null) {
            medicalTest.setQuantityOnHand(10);
        }

        if (medicalTest.getMinOnHand() == null) {
            medicalTest.setMinOnHand(10);
        }

    } //close method

    /**
     * Determines if the testcode that is passed in corresponds to an existing test.
     * @param testCode - The Medical test code
     * @return true if their is a MedicalTest with that testcode, false if there isn't
     */
    public boolean verifyTestCode(String testCode){
        Optional<MedicalTest> optionalMedicalTest = medicalTestRepository.findByTestCode(testCode);
        log.info("Test code is valid: {} ", optionalMedicalTest.isPresent());
        return optionalMedicalTest.isPresent();
    }

    /**
     * Log tests that are below their minimum inventory level
     */
    public void logLowInventoryMedicalTests(){

        //Find tests that are below their minimum inventory
        List<MedicalTest> lowTests = medicalTestRepository.findByTestLessThanMinOnHand();

        //if all tests are at a good level
        if(lowTests.size() == 0){
            log.info("All Medical Tests are above minimum inventory levels");
        }
        //If they aren't log them at a warning level
        else {
            log.warn("The follow tests are below minimum inventory levels");
            lowTests.stream()
                    .forEach(medTest ->
                            log.warn(medTest.getTestName()
                                    + " "
                                    + medTest.getTestCode()
                                    + " Tests on hand " + medTest.getQuantityOnHand()
                                    + " Minimum quantity " + medTest.getMinOnHand()
                            ));
        }
    } //close class

    /**
     * Add inventory to Medical Tests that are running low
     */
    public void addStockToMedicalTests(){
        List<MedicalTest> lowTests = medicalTestRepository.findByTestLessThanMinOnHand();
        lowTests.stream().forEach(medicalTest -> {
            medicalTest.setQuantityOnHand(medicalTest.getQuantityOnHand() + medicalTest.getQuantityToOrder());
            medicalTestRepository.save(medicalTest);
        });


    } //close method

}
