package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestResultEnum;
import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestStatusEnum;
import com.shiffler.AcmeTestingCenter.repository.MedicalTestOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
     * Saves orders for medical tests to the repository
     * @param medicalTestOrder - Describes the medical test being ordered
     * @return - A copy of the order being saved
     * @throws NoSuchElementException
     */
    @Override
    public MedicalTestOrder saveMedicalTestOrder(MedicalTestOrder medicalTestOrder) {

        log.info("Medical Test Order being submitted for {}", medicalTestOrder.toString());

        String testCode = medicalTestOrder.getTestCode();

        //Check to see if the testcode that was passed in is valid
        if (medicalTestService.verifyTestCode(testCode)) {


            //Retrieve the record for the specific Medical test so we can determine the inventory
            Optional<MedicalTest> optionalMedicalTest = medicalTestService.getMedicalTestByTestCode(testCode);
            MedicalTest medicalTest = optionalMedicalTest.get();

            //If there are tests on hand decrement the value available by one
            if (medicalTest.getQuantityOnHand() > 0){

                medicalTest.setQuantityOnHand(medicalTest.getQuantityOnHand() - 1);
                medicalTestService.saveMedicalTest(medicalTest);
                medicalTestOrder.setTestStatus(MedicalTestStatusEnum.ORDER_RECEIVED);
            }
            else {
                //If there are not tests on hand accept the order but set its status to on hold
                medicalTestOrder.setTestStatus(MedicalTestStatusEnum.ORDER_RECEIVED_ONHOLD);
            }

            //Set the result status and save the order
            medicalTestOrder.setMedicalTestResultEnum(MedicalTestResultEnum.WAITING_FOR_RESULT);
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
     * Updates num tests so that their status goes from being ORDER_RECEIVED_ONHOLD to TEST_IN_PROCESS.
     * The intention of this method is that when new testkit inventory are recieved a certain number of testorders
     * are taken off of hold status
     * @param num - The number of testsorders
     * @param testCode - which testCode to update
     *
     *
     * @return
     */
    @Override
    public List<MedicalTestOrder> updateOrderStatusOnHoldToTestInProcess(String testCode, int num) {
        return null;
    }


}
