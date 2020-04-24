/*
Implements all of the functionality available for the different MedicalTest Types.
 */

package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrderStatusEnum;
import com.shiffler.AcmeTestingCenter.repository.MedicalTestRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Data
public class MedicalTestServiceImpl implements MedicalTestService {

    private final MedicalTestRepository medicalTestRepository;
    private final MedicalTestOrderService medicalTestOrderService;

    private final float BASE_SCALE_FACTOR = 1.2f;
    float currentScaleFactor;

    @Value("${defaultQuantityToOrder}")
    int defaultQuantityToOrder;

    @Value("${defaultQuantityOnHand}")
    int defaultQuantityOnHand;

    @Value("${defaultMinOnHand}")
    int defaultMinOnHand;

    // @Lazy is used since both the medicalTestOrderService and the MedicalTestService rely on each other.
    // Here medicalTestOrderService is only used when its time to replenish MedicalTest supplies so we don't need it
    // right away
    @Autowired
    MedicalTestServiceImpl(MedicalTestRepository medicalTestRepository,
                           @Lazy MedicalTestOrderService medicalTestOrderService ){

        this.medicalTestRepository = medicalTestRepository;
        this.medicalTestOrderService = medicalTestOrderService;

        currentScaleFactor = BASE_SCALE_FACTOR; // initialize the currentScaleFactor to it's base level
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
    public MedicalTest saveMedicalTest(MedicalTest medicalTest) {

        initializeMedicalTest(medicalTest);
        return medicalTestRepository.save(medicalTest);
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
    public boolean isValidTestCode(String testCode){
        Optional<MedicalTest> optionalMedicalTest = medicalTestRepository.findByTestCode(testCode);
        log.debug("Test code is valid: {} ", optionalMedicalTest.isPresent());
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

        //Create a List of Medical Tests that are below the minimum inventory threshold
        List<MedicalTest> lowTests = medicalTestRepository.findByTestLessThanMinOnHand();

        lowTests.stream().forEach(medicalTest -> {

            //Find out if there are any orders on hold for the test
            long ordersOnHoldCount = medicalTestOrderService.
                    getTestCountByTestCodeAndStatus(
                            MedicalTestOrderStatusEnum.ORDER_PLACED_ONHOLD,
                            medicalTest.getTestCode());

            //If so order a scaling factor of the number of tests are on hold. We shouldn't be ordering tests
            // by the billions so casting should be ok.

            if(ordersOnHoldCount > medicalTest.getQuantityToOrder()) {

                int numTestsToOrder = (int) Math.round(ordersOnHoldCount * currentScaleFactor);

                log.info("Orders on hold, ordering {} tests ", numTestsToOrder);
                medicalTest.setQuantityOnHand(medicalTest.getQuantityOnHand() + numTestsToOrder);
                incrementCurrentScaleFactor(); // We've had orders on hold so we need to increase the scale factor
                                               // to keep up with demand.
            }else {
                medicalTest.setQuantityOnHand(medicalTest.getQuantityOnHand() + medicalTest.getQuantityToOrder());
                decrementCurrentScaleFactor(); // demand is waning, reduce the scale factor.
            }
            //Save the result
            medicalTestRepository.save(medicalTest);
        });


    } //close method

    /**
     * Contract the scale factor
     */
    private void decrementCurrentScaleFactor(){

        currentScaleFactor = currentScaleFactor - .1f;

        //currentScaleFactor can't be less than the BASE_SCALE_FACTOR
        if (currentScaleFactor < BASE_SCALE_FACTOR){
            currentScaleFactor = BASE_SCALE_FACTOR;
        }
        log.info("Decreasing scale factor to acquire new Medical Test stock to {}", currentScaleFactor);
    }

    /**
     * Expand the scale factor
     */
    private void incrementCurrentScaleFactor(){

        currentScaleFactor = currentScaleFactor + .1f;
        log.info("Increasing scale factor to acquire new Medical Test stock to {}", currentScaleFactor);

    }


}
