package com.shiffler.AcmeTestingCenter.cucumber.web.controller.stepdef;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.service.MedicalTestService;
import com.shiffler.AcmeTestingCenter.web.controller.MedicalTestController;
import com.shiffler.AcmeTestingCenter.web.mappers.MedicalTestMapper;
import com.shiffler.AcmeTestingCenter.web.model.MedicalTestDto;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedicalTestController.class)
@AutoConfigureWebMvc
public class MedicalTestControllerStepDefinitions {

    @Autowired
    @MockBean
    MedicalTestService medicalTestService;

    @Autowired
    @MockBean
    MedicalTestMapper medicalTestMapper;

    @Autowired
    MockMvc mockMvc;

    MedicalTest medicalTest1, medicalTest2, medicalTest3;
    MedicalTestDto medicalTestDto1, medicalTestDto2, medicalTestDto3;

    List<MedicalTestDto> medicalTestDtoList;
    List<MedicalTest> medicalTestList;


    @Before //Need to use the Cucumber @Before in order to make this work
            //Must be public so it runs.
    public void init(){

        System.out.println("Initializing $$$$$$$$$$$$$$$$$$$$$$$$");
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


    @When("^the client calls /medicaltests$")
    public void hello1() throws Exception {
        System.out.println(medicalTest2);
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
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


    }

    @Then("^the client receives status code of 200$")
    public void hello2(){
        System.out.println("hello2");
    }

    @And("^the client receives a list of the available medicaltests$")
    public void hello3(){
        System.out.println("hello3");

    }


}
