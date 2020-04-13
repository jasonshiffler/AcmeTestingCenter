package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
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

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MedicalTestOrderServiceImplUnitTest {

    @Mock
    MedicalTestOrderRepository medicalTestOrderRepository;

    @Mock
    MedicalTestService medicalTestService;

    @InjectMocks
    MedicalTestOrderServiceImpl medicalTestOrderService;

    MedicalTestOrder medicalTestOrder;

    @BeforeEach
    void init(){

        medicalTestOrder = new MedicalTestOrder();
        medicalTestOrder.setTestCode("00000A0002");

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

        //Given
        given(medicalTestService.isValidTestCode("00000A0002")).willReturn(false);

        //When/Then
        Assertions.assertThrows(NoSuchElementException.class,
                () -> medicalTestOrderService.saveMedicalTestOrder(medicalTestOrder));

    }




    @Test
    void processMedicalTestOrders() {
    }

    @Test
    void processTests() {
    }
}