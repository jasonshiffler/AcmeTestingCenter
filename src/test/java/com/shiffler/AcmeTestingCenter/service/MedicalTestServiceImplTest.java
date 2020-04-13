package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.repository.MedicalTestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalTestServiceImplTest {

    @Mock
    MedicalTestRepository medicalTestRepository;

    @InjectMocks
    MedicalTestServiceImpl medicalTestService;

    MedicalTest medicalTest1;
    MedicalTest medicalTest2;

    List<MedicalTest> medicalTestList;

    @BeforeEach
    public void init(){

        medicalTestService.setDefaultMinOnHand(10);
        medicalTestService.setDefaultQuantityOnHand(10);
        medicalTestService.setDefaultQuantityToOrder(10);
        medicalTest1 = new MedicalTest();
        medicalTest2 = new MedicalTest();

        medicalTestList = new ArrayList<>();
        medicalTestList.add(medicalTest1);
        medicalTestList.add(medicalTest2);

    }


    @Test
    @DisplayName("Find Medical Test By Id")
    void getMedicalTestById() {

        UUID uuid = UUID.fromString("984053ae-44a0-498d-ad22-3723142c0f0c");

        //When
        medicalTestService.getMedicalTestById(uuid);

        //Then
        then(medicalTestRepository).should().findById(uuid);


    }

    @Test
    @DisplayName("Find Medical Test By TestCode")
    void getMedicalTestByTestCode() {
        //When
        medicalTestService.getMedicalTestByTestCode("00000A0002");

        //Then
        then(medicalTestRepository).should().findByTestCode("00000A0002");
    }

    @Test
    @DisplayName("Save a Medical Test")
    void saveMedicalTest() {
        //When
        medicalTestService.saveMedicalTest(medicalTest1);

        //Then
        then(medicalTestRepository).should().save(any(MedicalTest.class));

        //Verify that the Medical Test is being initialized properly
        assertEquals(10,medicalTest1.getQuantityOnHand());
        assertEquals(10,medicalTest1.getQuantityToOrder());
        assertEquals(10,medicalTest1.getMinOnHand());

    }

    @Test
    @DisplayName("Find All Medical Tests")
    void getAllMedicalTests() {

        List<MedicalTest> results = new ArrayList<>();

        //Given
        given(medicalTestRepository.findAll()).willReturn(medicalTestList);

        //When
        medicalTestService.saveMedicalTest(medicalTest1);
        Iterable<MedicalTest> medicalTestIterable = medicalTestService.getAllMedicalTests();
        for(MedicalTest medicalTest: medicalTestIterable){
            results.add(medicalTest);
        }

        //Then
        then(medicalTestRepository).should().findAll();
        assertEquals(2, results.size());

    }

    @Test
    @DisplayName("Verify Medical Test Code")
    void verifyTestCode() {

        //When
        medicalTestService.isValidTestCode(anyString());

        //Then
        then(medicalTestRepository).should().findByTestCode(anyString());

    }

    @Test
    void logLowInventoryMedicalTests() {
    }

    @Test
    @DisplayName("Add Stock to Medical Tests")
    void addStockToMedicalTests() {
        //When
        medicalTestService.addStockToMedicalTests();

        //Then
        then(medicalTestRepository).should().findByTestLessThanMinOnHand();
    }
}