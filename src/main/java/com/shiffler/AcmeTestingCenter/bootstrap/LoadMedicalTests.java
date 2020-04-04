package com.shiffler.AcmeTestingCenter.bootstrap;


import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestStatusEnum;
import com.shiffler.AcmeTestingCenter.repository.MedicalTestOrderRepository;
import com.shiffler.AcmeTestingCenter.repository.MedicalTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LoadMedicalTests implements CommandLineRunner {

    MedicalTestRepository medicalTestRepository;
    MedicalTestOrderRepository medicalTestOrderRepository;

    @Autowired
    public LoadMedicalTests(MedicalTestRepository medicalTestRepository,
                            MedicalTestOrderRepository medicalTestOrderRepository){

        this.medicalTestRepository = medicalTestRepository;
        this.medicalTestOrderRepository = medicalTestOrderRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        //this.clearTable();
        this.createTests();
        this.createOrders();
    }


    @Transactional
    public void clearTable(){
        medicalTestRepository.deleteAll();
        medicalTestOrderRepository.deleteAll();
    }

    @Transactional
    public void createTests(){

            medicalTestRepository.save(MedicalTest.builder()
                    .testName("SARS-CoV-2")
                    .minOnHand(10)
                    .quantityOnHand(15)
                    .quantityToOrder(10)
                    .testCode("00000A0001")
                    .build()
            );

            medicalTestRepository.save(MedicalTest.builder()
                    .testName("Rapid Influenza Antigen")
                    .minOnHand(200)
                    .quantityOnHand(400)
                    .quantityToOrder(300)
                    .testCode("00000A0002")
                    .build()
            );

        medicalTestRepository.save(MedicalTest.builder()
                .testName("Ebola Antibody")
                .minOnHand(5)
                .quantityOnHand(10)
                .quantityToOrder(10)
                .testCode("00000A0003")
                .build()
        );

        medicalTestRepository.save(MedicalTest.builder()
                .testName("MERS-COV")
                .minOnHand(10)
                .quantityOnHand(14)
                .quantityToOrder(10)
                .testCode("00000A0004")
                .build()
        );
    }

    @Transactional
    public void createOrders() {
        MedicalTestOrder order = MedicalTestOrder.builder()
                .testCode("00000A0003")
                .testStatus(MedicalTestStatusEnum.ORDER_RECEIVED)
                .build();
        medicalTestOrderRepository.save(order);
    }
}
