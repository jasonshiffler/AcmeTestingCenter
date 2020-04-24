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

    @Mock
    MedicalTestOrderService medicalTestOrderService;

    @InjectMocks
    MedicalTestServiceImpl medicalTestService;

    MedicalTest medicalTest1, medicalTest2;
    MedicalTest medicalTest_AddStockToMedicalTests1; //This is to test the AddStockToMedicalTests method

    List<MedicalTest> medicalTestList;

    List<MedicalTest> medicalTestList_AddStockToMedicalTests1; //This is to test the AddStockToMedicalTests method

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


        //Initialization for AddStockToMedicalTests Tests
        medicalTest_AddStockToMedicalTests1 = new MedicalTest();
        medicalTest_AddStockToMedicalTests1.setMinOnHand(10);
        medicalTest_AddStockToMedicalTests1.setQuantityOnHand(5);
        medicalTest_AddStockToMedicalTests1.setQuantityToOrder(10);

        medicalTestList_AddStockToMedicalTests1 = new ArrayList<>();
        medicalTestList_AddStockToMedicalTests1.add(medicalTest_AddStockToMedicalTests1);

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
    @DisplayName("Add Stock to Medical Tests - No Orders on Hold")
    void addStockToMedicalTests_NoOrdersOnHold() {

        //Given - medicalTestList_AddStockToMedicalTests1 is the list that contains the tests that are low on inventory
        given(medicalTestRepository.findByTestLessThanMinOnHand()).willReturn(medicalTestList_AddStockToMedicalTests1);

        //Given - We want to mock that there are no orders on hold
        given(medicalTestOrderService.getTestCountByTestCodeAndStatus(any(),any())).willReturn(0L);

        //When
        medicalTestService.addStockToMedicalTests();

        //Then - The repository should save the updates
        then(medicalTestRepository).should().save(medicalTest_AddStockToMedicalTests1);

        //Then - qty on hand (5) should be added to the qty to order (10)
        assertEquals(15,medicalTest_AddStockToMedicalTests1.getQuantityOnHand());
    }

    @Test
    @DisplayName("Add Stock to Medical Tests - Orders on Hold")
    void addStockToMedicalTests_OrdersOnHold() {

        //Given - medicalTestList_AddStockToMedicalTests1 is the list that contains the tests that are low on inventory
        given(medicalTestRepository.findByTestLessThanMinOnHand()).willReturn(medicalTestList_AddStockToMedicalTests1);

        //Given - We want to mock that there are 30 orders on hold
        given(medicalTestOrderService.getTestCountByTestCodeAndStatus(any(),any())).willReturn(30L);

        //When
        medicalTestService.addStockToMedicalTests();

        //Then - The repository should save the updates
        then(medicalTestRepository).should().save(medicalTest_AddStockToMedicalTests1);

        //Then - qty on hand (5) should be added amount to order with should be 36
        assertEquals(41,medicalTest_AddStockToMedicalTests1.getQuantityOnHand());
    }

}