package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestResultEnum;
import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestStatusEnum;
import com.shiffler.AcmeTestingCenter.repository.MedicalTestOrderRepository;
import com.shiffler.AcmeTestingCenter.repository.MedicalTestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class MedicalTestOrderServiceImpl implements MedicalTestOrderService {

    private final MedicalTestRepository medicalTestRepository;
    private final MedicalTestOrderRepository medicalTestOrderRepository;

    @Autowired
    public MedicalTestOrderServiceImpl(MedicalTestRepository medicalTestRepository,
                                       MedicalTestOrderRepository medicalTestOrderRepository) {
        this.medicalTestRepository = medicalTestRepository;
        this.medicalTestOrderRepository = medicalTestOrderRepository;
    }

    /**
     * Allows a medical Test Order to be retrieved by the Id
     * @param id - The id of the MedicalTestOrder
     * @return
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
    public MedicalTestOrder saveMedicalTestOrder(MedicalTestOrder medicalTestOrder) throws NoSuchElementException {

        log.info("Medical Test Order being submitted for {}", medicalTestOrder.toString());

        String testCode = medicalTestOrder.getTestCode();

        //If the testcode that was passed in is valid
        if (verifyTestCode(testCode)) {


            //Retrieve the record for the specific Medical test so we can determine the inventory
            Optional<MedicalTest> optionalMedicalTest = medicalTestRepository.findByTestCode(testCode);
            MedicalTest medicalTest = optionalMedicalTest.get();

            //If there are tests on hand decrement the value available by one
            if (medicalTest.getQuantityOnHand() > 0){
                log.info("Medical test being analyzed is {}", medicalTest.toString() );
                medicalTest.setQuantityOnHand(medicalTest.getQuantityOnHand() - 1);
                log.info("Medical test being analyzed is {}", medicalTest.toString() );
                medicalTestRepository.save(medicalTest);
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
     * Determines if the Medical Test Code is a valid test
     * @param testCode - The Medical test code
     * @return true if the test is valid, false if it isn't
     */
    private boolean verifyTestCode(String testCode){
      Optional<MedicalTest> optionalMedicalTest = medicalTestRepository.findByTestCode(testCode);
      log.info("Test code is valid: {} ", optionalMedicalTest.isPresent());
      return optionalMedicalTest.isPresent();
    }




}
