package com.shiffler.AcmeTestingCenter.web.controller;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.service.MedicalTestService;
import com.shiffler.AcmeTestingCenter.web.mappers.MedicalTestMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;



@WebMvcTest(MedicalTestController.class)
class MedicalTestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MedicalTestMapper medicalTestMapper;

    @MockBean
    MedicalTestService medicalTestService;

    MedicalTest medicalTest1 = new MedicalTest();

    @BeforeEach
    void init(){
        medicalTest1 = new MedicalTest();
        medicalTest1.setId(UUID.fromString("984053ae-44a0-498d-ad22-3723142c0f0c"));
        medicalTest1.setQuantityOnHand(5);
        medicalTest1.setMinOnHand(5);
        medicalTest1.setQuantityToOrder(5);
        medicalTest1.setPrice(5.0f);
        medicalTest1.setTestCode("00000A001");
    }
    @AfterEach
    void tearDown(){
        reset(medicalTestService);
    }

    @Test
    void getMedicalTestById() throws Exception {


      }

    @Test
    void getAllMedicalTests() {
    }

    @Test
    void saveNewMedicalTest() {
    }
}