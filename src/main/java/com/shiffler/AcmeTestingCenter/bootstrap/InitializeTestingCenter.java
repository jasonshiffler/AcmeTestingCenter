/*
Initializes the program with the Medical Tests that are available.
 */


package com.shiffler.AcmeTestingCenter.bootstrap;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.entity.Organization;
import com.shiffler.AcmeTestingCenter.entity.User;
import com.shiffler.AcmeTestingCenter.repository.UserRepository;
import com.shiffler.AcmeTestingCenter.service.MedicalTestOrderService;
import com.shiffler.AcmeTestingCenter.service.MedicalTestService;
import com.shiffler.AcmeTestingCenter.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class InitializeTestingCenter implements CommandLineRunner {


    private final MedicalTestService medicalTestService;
    private final MedicalTestOrderService medicalTestOrderService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrganizationService organizationService;


    @Autowired
    public InitializeTestingCenter(MedicalTestService medicalTestService,
                                   MedicalTestOrderService medicalTestOrderService,
                                   UserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   OrganizationService organizationService){

        this.medicalTestService = medicalTestService;
        this.medicalTestOrderService = medicalTestOrderService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.organizationService = organizationService;
    }

    @Override
    public void run(String... args) {
        this.createMedicalTests();
        this.initializeUsers();
    }


    /**
     * Create Medical Tests that users can order.
     */
    public void createMedicalTests(){

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

    /*
    Create organizations that can use the service
     */
    public void initializeOrganizations(){

    }


    /**
     * Add some users to the database that we can authenticate against
     */
    public void initializeUsers(){

        Organization org1 = new Organization();
        org1.setOrganizationName("General Hospital");
        organizationService.saveOrganization(org1);

        Organization org2 = new Organization();
        org2.setOrganizationName("Specific Hospital");
        organizationService.saveOrganization(org2);


        User spadmin = new User("spadmin", passwordEncoder.encode("password"), "ADMIN", "",org2);
        User admin = new User("admin", passwordEncoder.encode("password"), "ADMIN", "",org1);
        List<User> users = Arrays.asList(spadmin,admin);
        this.userRepository.saveAll(users);
    }


} //close class
