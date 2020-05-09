package com.shiffler.AcmeTestingCenter.cucumber.steps;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.service.MedicalTestService;
import com.shiffler.AcmeTestingCenter.web.controller.MedicalTestController;
import com.shiffler.AcmeTestingCenter.web.mappers.MedicalTestMapper;
import com.shiffler.AcmeTestingCenter.web.model.MedicalTestDto;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = {"isSecurityOn=false"}) //Turn off Security while testing
@WebMvcTest(controllers = MedicalTestController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class RetrieveMedicalTestByIdStepDef {

    @Autowired
    @MockBean
    MedicalTestService medicalTestService;

    @Autowired
    @MockBean
    MedicalTestMapper medicalTestMapper;

    @Autowired
    MockMvc mockMvc;

    MedicalTest medicalTest1;
    MedicalTestDto medicalTestDto1;

    @Before //Need to use the Cucumber @Before in order to make this work
    //Must be public so it runs.
    public void init(){

        medicalTest1 = new MedicalTest();
        medicalTest1.setId(UUID.fromString("0276523c-de6d-43ae-a729-a501e28320dc"));
        medicalTest1.setMinOnHand(25);
        medicalTest1.setQuantityOnHand(35);
        medicalTest1.setQuantityToOrder(25);
        medicalTest1.setTestCode("00000A0002");
        medicalTest1.setTestName("Rapid Influenza Antigen");
        medicalTest1.setCost(2.25f);
        medicalTest1.setPrice(4.25f);

        medicalTestDto1 = new MedicalTestDto();
        medicalTestDto1.setTestName("Rapid Influenza Antigen");
        medicalTestDto1.setTestCode("00000A0002");
        medicalTestDto1.setId(UUID.fromString("0276523c-de6d-43ae-a729-a501e28320dc"));
        medicalTestDto1.setQuantityOnHand(35);
        medicalTestDto1.setPrice(4.25f);


    }
    @AfterEach
    void tearDown(){
        reset(medicalTestService);
    }




    @When("^the client calls /medicaltests/good_id$")
    public void client_calls_medicaltests_with_good_id() throws Exception {
        MvcResult result = mockMvc
                .perform(get("/api/v1/medicaltests/" + medicalTest1.getId())).andReturn();
    }



    @And("^the client receives a json representation of the medical test$")
    public void theClientReceivesAJsonRepresentationOfTheMedicalTest() throws Exception {

        //Given - Mock out the calls to the service layer and the Mapstruct Mapper
        given(medicalTestService
                .getMedicalTestById(any()))
                .willReturn(Optional.of(medicalTest1));

        given(medicalTestMapper
                .medicalTestToMedicalTestDto(medicalTest1))
                .willReturn(medicalTestDto1);

        //When - Run the REST call against the controller

        MvcResult result = mockMvc
                .perform(get("/api/v1/medicaltests/" + medicalTest1.getId()))

                //Then - The results should be as follow in the response

                .andExpect(jsonPath("$.id", is("0276523c-de6d-43ae-a729-a501e28320dc")))
                .andExpect(jsonPath("$.quantityOnHand", is(35)))
                .andExpect(jsonPath("$.testCode", is("00000A0002")))
                .andExpect(jsonPath("$.testName", is("Rapid Influenza Antigen")))
                .andExpect(jsonPath("$.price", is(4.25)))

                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }


    @When("^the client calls /medicaltests/bad_id$")
    public void theClientCallsMedicaltestsBad_id() throws Exception {

        //When - Run the REST call against the controller
        MvcResult result = mockMvc
                .perform(get("/api/v1/medicaltests/" + medicalTest1.getId())).andReturn();

    }


    @Then("^the client receives status code of 404$")
    public void theClientReceivesStatusCodeOf() throws Exception {
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
}
