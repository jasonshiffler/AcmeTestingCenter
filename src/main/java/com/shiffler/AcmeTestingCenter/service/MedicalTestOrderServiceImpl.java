package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrderStatusEnum;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestResultEnum;
import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
import com.shiffler.AcmeTestingCenter.repository.MedicalTestOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class MedicalTestOrderServiceImpl implements MedicalTestOrderService {

    private final MedicalTestService medicalTestService;
    private final MedicalTestOrderRepository medicalTestOrderRepository;

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
    public Optional<MedicalTestOrder> getMedicalTestOrderById(UUID id) {
        return medicalTestOrderRepository.findById(id);
    }

    /**
     * Saves orders for medical tests to the repository so they can be processed
     * @param medicalTestOrder - Describes the medical test being ordered
     * @return - A copy of the order being saved
     * @throws NoSuchElementException
     */
    @Override
    public MedicalTestOrder saveMedicalTestOrder(MedicalTestOrder medicalTestOrder) {

        log.info("Medical Test Order being submitted for {}", medicalTestOrder.toString());

        String testCode = medicalTestOrder.getTestCode();

        //Check to see if the testcode that was passed in is valid and if its a new order
        if (medicalTestService.verifyTestCode(testCode) && medicalTestOrder.getId() == null) {
           medicalTestOrder.setTestOrderStatus(MedicalTestOrderStatusEnum.ORDER_RECEIVED);
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
        processOrdersByStatus(MedicalTestOrderStatusEnum.ORDER_RECEIVED_ONHOLD);
        processOrdersByStatus(MedicalTestOrderStatusEnum.ORDER_RECEIVED);
    }

    /**
     * Look at all of the MedicalTestOrders with a specified status. If there are available tests set those
     * tests to be in process. If not set them to be on hold. We want to allow this to be done by status so
     * that orders maybe prioritized by status
     * @param medicalTestOrderStatusEnum
     */
    @Transactional(rollbackFor = Exception.class) //Don't allow transactions to complete if there are any Exceptions
    private void processOrdersByStatus(MedicalTestOrderStatusEnum medicalTestOrderStatusEnum){

        log.info("Processing Medical Test Orders with Status {}", medicalTestOrderStatusEnum);

        //Search for all Medical Tests of a specified status
        Iterable<MedicalTestOrder> medicalTestOrderIterable = medicalTestOrderRepository
                .findByOrderStatus(medicalTestOrderStatusEnum);

        //For each MedicalTestOrder

        for (MedicalTestOrder medicalTestOrder: medicalTestOrderIterable) {

            log.info("Processing order {}", medicalTestOrder.toString());

            //Retrieve the Medical Test that matches the testcode
            String testCode = medicalTestOrder.getTestCode();
            Optional<MedicalTest> optionalMedicalTest = medicalTestService.getMedicalTestByTestCode(testCode);
            MedicalTest medicalTest = optionalMedicalTest.get();

            //If there are available tests set the test to be in process and decrement the number of available tests
            if (medicalTest.getQuantityOnHand() > 0){
                log.info("Medical Tester Order set to TEST_IN_PROCESS Status");

                medicalTestOrder.setTestOrderStatus(MedicalTestOrderStatusEnum.TEST_IN_PROCESS);
                saveMedicalTestOrder(medicalTestOrder);

                medicalTest.setQuantityOnHand(medicalTest.getQuantityOnHand() - 1);
                log.info("Medical test {} has {} testkits remaining"
                        , medicalTest.getTestName(), medicalTest.getQuantityOnHand());
                medicalTestService.saveMedicalTest(medicalTest);
                return;

            }
            //If there are no available tests
            else if (medicalTestOrder.getTestOrderStatus() == MedicalTestOrderStatusEnum.ORDER_RECEIVED){
                log.info("Medical Test Inventory Depleted Order Status Set to ORDER_RECEIVED_ONHOLD");
                medicalTestOrder.setTestOrderStatus(MedicalTestOrderStatusEnum.ORDER_RECEIVED_ONHOLD);
                saveMedicalTestOrder(medicalTestOrder);
                return;
            }
            else if (medicalTestOrder.getTestOrderStatus() == MedicalTestOrderStatusEnum.ORDER_RECEIVED_ONHOLD){
                log.info("Medical Test Inventory Depleted Order Status remains at to ORDER_RECEIVED_ONHOLD");
                return;
            }

        } //close for

    } //close method

}
