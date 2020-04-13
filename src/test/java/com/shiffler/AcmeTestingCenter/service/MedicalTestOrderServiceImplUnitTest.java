package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrderStatusEnum;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestResultEnum;
import com.shiffler.AcmeTestingCenter.repository.MedicalTestOrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicalTestOrderServiceImplUnitTest {

    @Mock
    MedicalTestOrderRepository medicalTestOrderRepository;

    @Mock
    MedicalTestService medicalTestService;

    @InjectMocks
    MedicalTestOrderServiceImpl medicalTestOrderService;

    MedicalTestOrder medicalTestOrder;

    MedicalTest medicalTest; //This medical test has inventory available.

    MedicalTest medicalTestEmpty; // This one is out of testkits

    List<MedicalTestOrder> medicalTestOrderList;
    MedicalTestOrder medicalTestOrderEntry1; //This is for a test where the test is currently being conducted
    MedicalTestOrder medicalTestOrderEntry2; //This


    @BeforeEach
    void init(){

        //Initialized two MedicalTestOrders for testing
        medicalTestOrder = new MedicalTestOrder();
        medicalTestOrder.setTestCode("00000A0002");

        medicalTestOrderEntry1 = new MedicalTestOrder();
        medicalTestOrderEntry1.setId(100000000L);
        medicalTestOrderEntry1.setTestCode("00000A0002");
        medicalTestOrderEntry1.setTestOrderStatusEnum(MedicalTestOrderStatusEnum.TEST_IN_PROCESS);
        medicalTestOrderEntry1.setMedicalTestResultEnum(MedicalTestResultEnum.WAITING_FOR_RESULT);

        medicalTestOrderEntry2 = new MedicalTestOrder();
        medicalTestOrderEntry2.setId(100000000L);
        medicalTestOrderEntry2.setTestCode("00000A0002");
        medicalTestOrderEntry2.setTestOrderStatusEnum(MedicalTestOrderStatusEnum.ORDER_PLACED);
        medicalTestOrderEntry2.setMedicalTestResultEnum(MedicalTestResultEnum.WAITING_FOR_RESULT);



        //Initialize a List of MedicalTests that will be used as an Iterable
        medicalTestOrderList = new ArrayList<>();
        medicalTestOrderList.add(medicalTestOrderEntry1);

        //Initialize a Medical Test that will be used in testing
        medicalTest = new MedicalTest();
        medicalTest.setTestName("Rapid Influenza Antigen");
        medicalTest.setMinOnHand(200);
        medicalTest.setQuantityToOrder(300);
        medicalTest.setQuantityOnHand(250);
        medicalTest.setPrice(5.75f);
        medicalTest.setCost(2.05f);
        medicalTest.setTestCode("00000A0002");

        //Initialize a Medical Test that will be used in testing
        medicalTestEmpty = new MedicalTest();
        medicalTestEmpty.setTestName("Rapid Influenza Antigen");
        medicalTestEmpty.setMinOnHand(200);
        medicalTestEmpty.setQuantityToOrder(300);
        medicalTestEmpty.setQuantityOnHand(0);
        medicalTestEmpty.setPrice(5.75f);
        medicalTestEmpty.setCost(2.05f);
        medicalTestEmpty.setTestCode("00000A0002");

    }


    @Test
    void getMedicalTestOrderById() {

        //When- We search for the MedicalTestOrder by id
        medicalTestOrderService.getMedicalTestOrderById(anyLong());

        //Then - The Repository should call its findById method and then should be finished.
        then(medicalTestOrderRepository).should(times(1)).findById(anyLong());
        then(medicalTestOrderRepository).shouldHaveNoMoreInteractions();

    }

    @Test
    @DisplayName("Testing saveMedicalTestOrder - New Test")
    void saveNewMedicalTestOrder() {

        //Given
        given(medicalTestService.isValidTestCode("00000A0002")).willReturn(true);

        //When
        medicalTestOrderService.saveMedicalTestOrder(medicalTestOrder);

        //Then
        assertEquals(MedicalTestOrderStatusEnum.ORDER_PLACED, medicalTestOrder.getTestOrderStatusEnum());
        assertEquals(MedicalTestResultEnum.WAITING_FOR_RESULT, medicalTestOrder.getMedicalTestResultEnum());
        then(medicalTestOrderRepository).should(times(1)).save(medicalTestOrder);
        then(medicalTestOrderRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("Testing saveMedicalTestOrder - Existing Test")
    void saveExistingMedicalTestOrder() {

        medicalTestOrder.setId(1L);

        //Given
        given(medicalTestService.isValidTestCode("00000A0002")).willReturn(true);

        //When
        medicalTestOrderService.saveMedicalTestOrder(medicalTestOrder);

        //Then
        then(medicalTestOrderRepository).should(times(1)).save(medicalTestOrder);
        then(medicalTestOrderRepository).shouldHaveNoMoreInteractions();
    }


    @Test
    @DisplayName("Testing saveMedicalTestOrder - Invalid Test Code")
    void saveMedicalTestOrderExceptionTest() {

        //Given - When we validate the test code it will be invalid
        given(medicalTestService.isValidTestCode("00000A0002")).willReturn(false);

        //When/Then - Trying to save a MedicalTestOrder with an invalid test code should cause an exception to be
        //            thrown.
        Assertions.assertThrows(NoSuchElementException.class,
                () -> medicalTestOrderService.saveMedicalTestOrder(medicalTestOrder));

    }


    @Test
    void processMedicalTestOrders() {

        //When - We process the orders
        medicalTestOrderService.processMedicalTestOrders();

        //Then - All orders that are in ORDER_PLACED or ORDER_PLACED_ONHOLD status should be retrieved
        then(medicalTestOrderRepository)
                .should(times(1))
                .findByOrderStatus(MedicalTestOrderStatusEnum.ORDER_PLACED_ONHOLD);
        then(medicalTestOrderRepository)
                .should(times(1))
                .findByOrderStatus(MedicalTestOrderStatusEnum.ORDER_PLACED);

    }

    @Test
    @DisplayName("Testing processOrder - There are available test kits for the type of test needed")
    void processOrderWithTestsAvailable(){

        //Given - This is a valid test code and the Medical Test that is being compared against
        given(medicalTestService.isValidTestCode("00000A0002")).willReturn(true);
        given(medicalTestService.getMedicalTestByTestCode("00000A0002")).willReturn(Optional.of(medicalTest));

        //When - We process an order
        medicalTestOrderService.processOrder(medicalTestOrderEntry2);

        //Then - Order status should have been updated, the available medical tests decremented and the new
        //       MedicalTest state saved
        assertEquals(MedicalTestOrderStatusEnum.TEST_IN_PROCESS,medicalTestOrderEntry2.getTestOrderStatusEnum());
        assertEquals(249, medicalTest.getQuantityOnHand());
        then(medicalTestService)
                .should(times(1))
                .saveMedicalTest(medicalTest);
    }

    @Test
    @DisplayName("Testing processOrder - There are no test available kits for the type of test needed")
    void processOrderWithNoTestsAvailable(){

        //Given - This is a valid test code and the Medical Test that is being compared against

        given(medicalTestService.isValidTestCode("00000A0002")).willReturn(true);
        given(medicalTestService.getMedicalTestByTestCode("00000A0002")).willReturn(Optional.of(medicalTestEmpty));

        //When - We process an order
        medicalTestOrderService.processOrder(medicalTestOrderEntry2);

        //Then - Order status should have been updated, the available medical tests decremented and the new
        //       MedicalTest state saved
        assertEquals(MedicalTestOrderStatusEnum.ORDER_PLACED_ONHOLD, medicalTestOrderEntry2.getTestOrderStatusEnum());

    }



    @Test
    void processTests() {

        //Given - When we retrieve Orders from the repository return the List of MedicalTestOrders
        given(medicalTestOrderRepository
                .findByOrderStatus(MedicalTestOrderStatusEnum.TEST_IN_PROCESS))
                .willReturn(medicalTestOrderList);

        //When - The tests are processed
        medicalTestOrderService.processTests();

        //Then -
        then(medicalTestOrderRepository)
                .should(times(1))
                .save(medicalTestOrderEntry1);

        assertEquals(MedicalTestOrderStatusEnum.COMPLETE,medicalTestOrderEntry1.getTestOrderStatusEnum());
        assertNotEquals(MedicalTestOrderStatusEnum.TEST_IN_PROCESS,medicalTestOrderEntry1.getMedicalTestResultEnum());

    }
}