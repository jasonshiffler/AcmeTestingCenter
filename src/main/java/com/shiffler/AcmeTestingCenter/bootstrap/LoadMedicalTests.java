/*
Initializes the program with the Medical Tests that are available.
 */


package com.shiffler.AcmeTestingCenter.bootstrap;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.service.MedicalTestOrderService;
import com.shiffler.AcmeTestingCenter.service.MedicalTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


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
    } //close method

} //close class
