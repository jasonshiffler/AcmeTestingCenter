/*
This class controls the flow of the program by doing the following:

-Manage Test Kit Inventory
-Process Orders
-Process Tests

 */

package com.shiffler.AcmeTestingCenter.programflow;

import com.shiffler.AcmeTestingCenter.service.MedicalTestOrderService;
import com.shiffler.AcmeTestingCenter.service.MedicalTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
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

    }
}
