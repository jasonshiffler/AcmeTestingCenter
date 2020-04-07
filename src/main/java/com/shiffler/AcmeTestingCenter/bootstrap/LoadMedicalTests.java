/*
Performs state initialization
 */


package com.shiffler.AcmeTestingCenter.bootstrap;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrderStatusEnum;
import com.shiffler.AcmeTestingCenter.service.MedicalTestOrderService;
import com.shiffler.AcmeTestingCenter.service.MedicalTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
public class LoadMedicalTests implements CommandLineRunner {


    private final MedicalTestService medicalTestService;
    private final MedicalTestOrderService medicalTestOrderService;

    @Autowired
    public LoadMedicalTests(MedicalTestService medicalTestService,
                            MedicalTestOrderService medicalTestOrderService){

        this.medicalTestService = medicalTestService;
        this.medicalTestOrderService = medicalTestOrderService;
    }

    @Override
    public void run(String... args) {
        this.createTests();
        this.createOrders();
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

    public void createTests(){

            medicalTestService.saveMedicalTest(MedicalTest.builder()
                    .testName("SARS-CoV-2")
                    .minOnHand(10)
                    .quantityOnHand(15)
                    .quantityToOrder(10)
                    .price(1.25f)
                    .cost(.25f)
                    .testCode("00000A0001")
                    .build()
            );

        medicalTestService.saveMedicalTest(MedicalTest.builder()
                    .testName("Rapid Influenza Antigen")
                    .minOnHand(200)
                    .quantityOnHand(400)
                    .quantityToOrder(300)
                    .price(5.75f)
                    .cost(2.05f)
                    .testCode("00000A0002")
                    .build()
            );

        medicalTestService.saveMedicalTest(MedicalTest.builder()
                .testName("Ebola Antibody")
                .minOnHand(5)
                .quantityOnHand(10)
                .quantityToOrder(10)
                .price(3.17f)
                .cost(1.25f)
                .testCode("00000A0003")
                .build()
        );

        medicalTestService.saveMedicalTest(MedicalTest.builder()
                .testName("MERS-COV")
                .minOnHand(10)
                .quantityOnHand(14)
                .quantityToOrder(10)
                .price(10.25f)
                .cost(6.25f)
                .testCode("00000A0004")
                .build()
        );
    }

    public void createOrders() {

        IntStream.rangeClosed(1,1000).forEach( s ->
                {
        MedicalTestOrder order = MedicalTestOrder.builder()
                .testCode("00000A0003")
                .testOrderStatus(MedicalTestOrderStatusEnum.ORDER_RECEIVED)
                .build();
        medicalTestOrderService.saveMedicalTestOrder(order);}
        );
    }
}
