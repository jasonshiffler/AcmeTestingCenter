package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrderStatusEnum;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestResultEnum;
import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
import com.shiffler.AcmeTestingCenter.repository.MedicalTestOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class MedicalTestOrderServiceImpl implements MedicalTestOrderService {

    private final MedicalTestService medicalTestService;
    private final MedicalTestOrderRepository medicalTestOrderRepository;

    @Value("${negative.result.probability}")
    double negativeResultProbability;

    @Value("${inconclusive.result.probability}")
    double inconclusiveResultProbability;

    @Autowired
    public MedicalTestOrderServiceImpl(MedicalTestService medicalTestService,
                                       MedicalTestOrderRepository medicalTestOrderRepository) {
        this.medicalTestService = medicalTestService;
        this.medicalTestOrderRepository = medicalTestOrderRepository;
    }

    /**
     * Allows a medical Test Order to be retrieved by id
     * @param id - The id of the MedicalTestOrder
     * @return An Optional of the MedicalTestOrder that matches the id
     */
    @Override
    public Optional<MedicalTestOrder> getMedicalTestOrderById(Long id) {
        return medicalTestOrderRepository.findById(id);
    }

    /**
     * Saves orders for medical tests to the repository so they can be processed. This works for orders that are new
     * or need to be updated.
     * @param medicalTestOrder - Describes the medical test being ordered
     * @return - A copy of the order being saved
     * @throws NoSuchElementException
     */
    @Override
    public MedicalTestOrder saveMedicalTestOrder(MedicalTestOrder medicalTestOrder) {

        log.info(" New Medical Test Order being submitted for {}", medicalTestOrder.toString());

        String testCode = medicalTestOrder.getTestCode();

        //Check to see if the testcode that was passed in is valid and if its a new order
        if (medicalTestService.verifyTestCode(testCode) && medicalTestOrder.getId() == null) {
           medicalTestOrder.setTestOrderStatusEnum(MedicalTestOrderStatusEnum.ORDER_PLACED);
           medicalTestOrder.setMedicalTestResultEnum(MedicalTestResultEnum.WAITING_FOR_RESULT);
           return medicalTestOrderRepository.save(medicalTestOrder);
        }

        //Don't update status for existing orders
        else if(medicalTestService.verifyTestCode(testCode) && medicalTestOrder.getId() != null){
            return medicalTestOrderRepository.save(medicalTestOrder);
        }

        //if the test code isn't valid
        else {
            log.error("Medical Test order used invalid test code {} ", medicalTestOrder.getId().toString() +
                    medicalTestOrder.getTestCode().toString());
            throw new NoSuchElementException("The medical test code does not exist");
        }
    }


    /**
     * Looks at all of the Medical Test orders that have been received. If the test is in ORDER_RECEIVED status it
     * will be put on hold if there are insufficient test kits or set to test in process of there is an available kit.
     */
    public void processMedicalTestOrders(){
        log.info("Processing Medical Test Orders");

        //Process the on hold orders first
        processOrdersByStatus(MedicalTestOrderStatusEnum.ORDER_PLACED_ONHOLD);
        processOrdersByStatus(MedicalTestOrderStatusEnum.ORDER_PLACED);
    }

    /**
     * Look at all of the MedicalTestOrders with a specified status. If there are available tests set those
     * tests to be in process. If not set them to be on hold. We want to allow this to be done by status so
     * that orders maybe prioritized by status
     * @param medicalTestOrderStatusEnum
     */

    private void processOrdersByStatus(MedicalTestOrderStatusEnum medicalTestOrderStatusEnum){

        log.info("Processing Medical Test Orders with Status {}", medicalTestOrderStatusEnum);

        //Search for all Medical Tests of a specified status
        List<MedicalTestOrder> medicalTestOrderList = medicalTestOrderRepository
                .findByOrderStatus(medicalTestOrderStatusEnum);

        //For each MedicalTestOrder

        for (MedicalTestOrder medicalTestOrder: medicalTestOrderList) {
            processOneOrderByStatus(medicalTestOrder);

        } //close for

    } //close method


    /**
     * Handles the processing of a single order so a single order can be done as a transaction.
     * @param medicalTestOrder - The Medical Test Order that's being processed
     *
     */
    @Transactional(rollbackFor = Exception.class) //Don't allow transactions to complete if there are any Exceptions
    private void processOneOrderByStatus(MedicalTestOrder medicalTestOrder)    {

        log.info("Processing order {}", medicalTestOrder.toString());

        //Retrieve the Medical Test that matches the testcode
        String testCode = medicalTestOrder.getTestCode();
        Optional<MedicalTest> optionalMedicalTest = medicalTestService.getMedicalTestByTestCode(testCode);
        MedicalTest medicalTest = optionalMedicalTest.get();

        //If there are available tests set the test to be in process and decrement the number of available tests
        if (medicalTest.getQuantityOnHand() > 0){
            log.info("Medical Tester Order set to TEST_IN_PROCESS Status");

            medicalTestOrder.setTestOrderStatusEnum(MedicalTestOrderStatusEnum.TEST_IN_PROCESS);
            saveMedicalTestOrder(medicalTestOrder);

            medicalTest.setQuantityOnHand(medicalTest.getQuantityOnHand() - 1);
            log.info("Medical test {} has {} testkits remaining"
                    , medicalTest.getTestName(), medicalTest.getQuantityOnHand());
            medicalTestService.saveMedicalTest(medicalTest);
        }

        //If there are no available tests
        else if (medicalTestOrder.getTestOrderStatusEnum() == MedicalTestOrderStatusEnum.ORDER_PLACED){
            log.info("Medical Test Inventory Depleted Order Status Set to ORDER_PLACED_ONHOLD");
            medicalTestOrder.setTestOrderStatusEnum(MedicalTestOrderStatusEnum.ORDER_PLACED_ONHOLD);
            saveMedicalTestOrder(medicalTestOrder);

        }
        else if (medicalTestOrder.getTestOrderStatusEnum() == MedicalTestOrderStatusEnum.ORDER_PLACED_ONHOLD){
            log.info("Medical Test Inventory Depleted Order Status remains at to ORDER_PLACED_ONHOLD");

        }

    }// close method



    /**
     * Looks at all of the Medical Test Orders that are in TEST_IN_PROCESS status. "Runs the test" and then enters
     * the result and sets the status to COMPLETE
     */
    public void processTests(){

        log.info("Processing Medical Tests");

        //Get all Medical Test Orders that are in TEST_IN_PROCESS STATUS
        List<MedicalTestOrder> medicalTestOrderIterable = medicalTestOrderRepository
                .findByOrderStatus(MedicalTestOrderStatusEnum.TEST_IN_PROCESS);

        //Iterate over the results, set the test result status and mark the order status to be complete
        medicalTestOrderIterable.forEach( medicalTestOrder -> {
            medicalTestOrder.setMedicalTestResultEnum(generateRandomMedicalTestResult());
            medicalTestOrder.setTestOrderStatusEnum(MedicalTestOrderStatusEnum.COMPLETE);
            log.info(medicalTestOrder.getId()
                    + " "
                    + medicalTestOrder.getTestCode()
                    + " is now "
                    + medicalTestOrder.getTestOrderStatusEnum());
            log.info(medicalTestOrder.getId()
                    + " "
                    + medicalTestOrder.getTestCode()
                    + " result is "
                    + medicalTestOrder.getMedicalTestResultEnum());
            medicalTestOrderRepository.save(medicalTestOrder);

        });

        log.info("Processing Medical Tests is complete");

    } //close method

    /**
     * Generates a random MedicalTestResult. Need to add validation here
     * @return a MedicalTestResultEnum of a random value
     */
    private MedicalTestResultEnum generateRandomMedicalTestResult(){

        double result = Math.random();

        if (result < negativeResultProbability)
            return MedicalTestResultEnum.NEGATIVE;
        else if (result > negativeResultProbability && result < 1 - inconclusiveResultProbability)
            return MedicalTestResultEnum.POSITIVE;
        else
            return MedicalTestResultEnum.INCONCLUSIVE;
    }


}
