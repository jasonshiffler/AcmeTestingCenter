/*
This class handles reporting on and replenishing the stock of tests that are running low.
 */

package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.repository.MedicalTestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class ReplenishTestsImpl {

    private final MedicalTestRepository medicalTestRepository;


    @Autowired
    ReplenishTestsImpl(MedicalTestRepository medicalTestRepository){
        this.medicalTestRepository = medicalTestRepository;
    }

    /**
     * Log tests that are below their minimum inventory level
     */
    @Scheduled(cron = " 10,20,30,40,50 * * * * *")
    public void printLowTests(){

        List<MedicalTest> lowTests = medicalTestRepository.findByTestLessThanMinOnHand();
        lowTests.stream().forEach(medTest -> log.info(medTest.toString()));
    }

    /**
     * Add inventory to tests that are running low
     */
    @Scheduled(cron = " 30 * * * * *")
    public void AddStockToTests(){
        List<MedicalTest> lowTests = medicalTestRepository.findByTestLessThanMinOnHand();
        lowTests.stream().forEach(medicalTest -> {
                    medicalTest.setQuantityOnHand(medicalTest.getQuantityOnHand() + medicalTest.getQuantityToOrder());
                    medicalTestRepository.save(medicalTest);
        });


    }



}
