package com.shiffler.AcmeTestingCenter.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.service.MedicalTestService;
import com.shiffler.AcmeTestingCenter.web.mappers.MedicalTestMapper;
import com.shiffler.AcmeTestingCenter.web.model.MedicalTestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicalTestController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class )//Disable Security for testing purposes)
class MedicalTestControllerTest {

    @MockBean
    MedicalTestService medicalTestService;

    @MockBean
    MedicalTestMapper medicalTestMapper;

    @Autowired
    MockMvc mockMvc;

    MedicalTest medicalTest1, medicalTest2, medicalTest3;
    MedicalTestDto medicalTestDto1, medicalTestDto2, medicalTestDto3;

    List<MedicalTestDto> medicalTestDtoList;
    List<MedicalTest> medicalTestList;

    @BeforeEach
    void init(){

        //Initialize the medicalTest and medicalTestDto that we will use
        medicalTest1 = new MedicalTest();
        medicalTest1.setId(UUID.fromString("cc3fabfa-f6ca-4fcb-a829-6b33a6d8bb3e"));
        medicalTest1.setMinOnHand(25);
        medicalTest1.setQuantityOnHand(50);
        medicalTest1.setQuantityToOrder(25);
        medicalTest1.setTestCode("00000A0001");
        medicalTest1.setTestName("SARS-CoV-2");
        medicalTest1.setCost(2.25f);
        medicalTest1.setPrice(4.25f);

        medicalTestDto1 = new MedicalTestDto();
        medicalTestDto1.setTestName("SARS-CoV-2");
        medicalTestDto1.setTestCode("00000A0001");
        medicalTestDto1.setId(UUID.fromString("cc3fabfa-f6ca-4fcb-a829-6b33a6d8bb3e"));
        medicalTestDto1.setQuantityOnHand(50);
        medicalTestDto1.setPrice(4.25f);

        medicalTest2 = new MedicalTest();
        medicalTest2.setId(UUID.fromString("0276523c-de6d-43ae-a729-a501e28320dc"));
        medicalTest2.setMinOnHand(25);
        medicalTest2.setQuantityOnHand(35);
        medicalTest2.setQuantityToOrder(25);
        medicalTest2.setTestCode("00000A0002");
        medicalTest2.setTestName("Rapid Influenza Antigen");
        medicalTest2.setCost(2.25f);
        medicalTest2.setPrice(4.25f);

        medicalTestDto2 = new MedicalTestDto();
        medicalTestDto2.setTestName("Rapid Influenza Antigen");
        medicalTestDto2.setTestCode("00000A0002");
        medicalTestDto2.setId(UUID.fromString("0276523c-de6d-43ae-a729-a501e28320dc"));
        medicalTestDto2.setQuantityOnHand(35);
        medicalTestDto2.setPrice(4.25f);

        //This one is for the POST operation which doesn't allow an id or qty to be specified.

        medicalTestDto3 = new MedicalTestDto();
        medicalTestDto3.setTestName("Ebola Antibody");
        medicalTestDto3.setTestCode("00000A0003");

        medicalTest3 = new MedicalTest();
        medicalTest3.setId(UUID.fromString("f1d12aae-1e44-49b5-96f6-c24cbfad7f82"));
        medicalTest3.setMinOnHand(5);
        medicalTest3.setQuantityOnHand(10);
        medicalTest3.setQuantityToOrder(5);
        medicalTest3.setTestCode("00000A0003");
        medicalTest3.setTestName("Ebola Antibody");
        medicalTest3.setCost(2.25f);
        medicalTest3.setPrice(4.25f);

        //Initialize and populate the List of MedicalTestDto objects
        medicalTestDtoList = new ArrayList<>();
        medicalTestDtoList.add(medicalTestDto1);
        medicalTestDtoList.add(medicalTestDto2);

        //Initialize and populate the List of MedicalTest objects
        medicalTestList = new ArrayList<>();
        medicalTestList.add(medicalTest1);
        medicalTestList.add(medicalTest2);
    }

    @AfterEach
    void tearDown(){
        reset(medicalTestService);
    }


    @Test
    @DisplayName("Test getMedicalTestByIdFound - Element is found")
    void getMedicalTestById() throws Exception {

        //Given - Mock out the calls to the service layer and the Mapstruct Mapper
        given(medicalTestService
                .getMedicalTestById(any()))
                .willReturn(Optional.of(medicalTest2));

        given(medicalTestMapper
                .medicalTestToMedicalTestDto(medicalTest2))
                .willReturn(medicalTestDto2);

        //When - Run the REST call against the controller

        MvcResult result = mockMvc
                .perform(get("/api/v1/medicaltests/" + medicalTest2.getId()))

        //Then - The results should be as follow in the response

                .andExpect(jsonPath("$.id", is("0276523c-de6d-43ae-a729-a501e28320dc")))
                .andExpect(jsonPath("$.quantityOnHand", is(35)))
                .andExpect(jsonPath("$.testCode", is("00000A0002")))
                .andExpect(jsonPath("$.testName", is("Rapid Influenza Antigen")))
                .andExpect(jsonPath("$.price", is(4.25)))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("Test getMedicalTestByIdNotFound - Element isn't found")
    void getMedicalTestByIdNotFound() throws Exception {

        //Given - Mock out the calls to the service layer which will not find the element
        given(medicalTestService
                .getMedicalTestById(any()))
                .willThrow(new NoSuchElementException());

        //When - Run the REST call against the controller
        MvcResult result = mockMvc
                .perform(get("/api/v1/medicaltests/" + medicalTest1.getId()))

        //Then - Should be a 404 not found
        .andExpect(status().isNotFound()).andReturn();
    }


    @Test
    @DisplayName("getAllMedicalTests - Returns two elements")
    void getAllMedicalTests() throws Exception {

        //Given - Mock out the calls to the service layer and the Mapstruct Mapper

        given(medicalTestService.getAllMedicalTests())
                .willReturn(medicalTestList);

        given(medicalTestMapper
                .medicalTestIterableToMedicalTestDtoIterable(medicalTestList))
                .willReturn(medicalTestDtoList);

        //When - Run the REST call against the controller
        MvcResult result = mockMvc
                .perform(get("/api/v1/medicaltests/" ))

        //Then - Should be a list of values

                .andExpect(jsonPath("$[0].id", is("cc3fabfa-f6ca-4fcb-a829-6b33a6d8bb3e")))
                .andExpect(jsonPath("$[0].quantityOnHand", is(50)))
                .andExpect(jsonPath("$[0].testCode", is("00000A0001")))
                .andExpect(jsonPath("$[0].testName", is("SARS-CoV-2")))
                .andExpect(jsonPath("$[0].price", is(4.25)))

                .andExpect(jsonPath("$[1].id", is("0276523c-de6d-43ae-a729-a501e28320dc")))
                .andExpect(jsonPath("$[1].quantityOnHand", is(35)))
                .andExpect(jsonPath("$[1].testCode", is("00000A0002")))
                .andExpect(jsonPath("$[1].testName", is("Rapid Influenza Antigen")))
                .andExpect(jsonPath("$[1].price", is(4.25)))

                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("saveMedicalTest - Returns the location of the new test")
    void saveNewMedicalTest() throws Exception {

        //Given - We're mocking the mapper so we have to "map" manually
        given(medicalTestMapper
                .medicalTestDtoToMedicalTest(any()))
                .willReturn(medicalTest3);

        given(medicalTestService.saveMedicalTest(medicalTest3)).willReturn(medicalTest3);

        //When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/medicaltests")

        //Then
                //Map the POJO to a JSON Format
                .content(new ObjectMapper().writeValueAsString(medicalTestDto3))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        //Validate the location field tha we should get back that tells us the location of our new MedicalTestOrder
        String headervalue = result.getResponse().getHeader("Location");
        assertTrue(headervalue.equals("http://localhost:8081/api/v1/medicaltests/f1d12aae-1e44-49b5-96f6-c24cbfad7f82"));
    }
}