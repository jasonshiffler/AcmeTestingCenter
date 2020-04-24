/*
This class controls the flow of the program by doing the following:

-Manage Test Kit Inventory
-Process Orders
-Process Tests

 */

package com.shiffler.AcmeTestingCenter.programflow;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrderStatusEnum;
import com.shiffler.AcmeTestingCenter.service.MedicalTestOrderService;
import com.shiffler.AcmeTestingCenter.service.MedicalTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProgramFlow {


    private final MedicalTestService medicalTestService;
    private final MedicalTestOrderService medicalTestOrderService;

    @Autowired
    public ProgramFlow(MedicalTestService medicalTestService, MedicalTestOrderService medicalTestOrderService) {
        this.medicalTestService = medicalTestService;
        this.medicalTestOrderService = medicalTestOrderService;
    }

    @Scheduled(cron = " 30 * * * * *")
    public void programFlow(){

        //Log the current status of Medical Test Kit Inventory
        medicalTestService.logLowInventoryMedicalTests();

        //Replenish depleted Medical Tests
        medicalTestService.addStockToMedicalTests();

        //Look through Medical Test Orders that are on hold due to lack of inventory
        //If there are testkits that are now available change the status and update the testkit inventory

        medicalTestOrderService.processMedicalTestOrders();

        //Process all of the tests by setting the test result and the order status
        medicalTestOrderService.processTests();

        //Provide current status
        log.info("Number of tests on hold: {}", medicalTestOrderService
                        .getTestCountByStatus(MedicalTestOrderStatusEnum.ORDER_PLACED_ONHOLD));
        log.info("Number of orders placed in process: {}",medicalTestOrderService
                .getTestCountByStatus(MedicalTestOrderStatusEnum.ORDER_PLACED));
        log.info("Number of tests complete: {}", medicalTestOrderService
                .getTestCountByStatus(MedicalTestOrderStatusEnum.COMPLETE));

    }
}
