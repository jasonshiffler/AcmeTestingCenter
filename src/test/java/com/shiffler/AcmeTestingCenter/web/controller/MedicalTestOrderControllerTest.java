//These are integration tests for the MedicalTestOrder Controller.
//All dependencies of this class are mocked to keep it as light weight as possible.

package com.shiffler.AcmeTestingCenter.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrderStatusEnum;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestResultEnum;
import com.shiffler.AcmeTestingCenter.service.MedicalTestOrderService;
import com.shiffler.AcmeTestingCenter.service.UserService;
import com.shiffler.AcmeTestingCenter.service.UserServiceImpl;
import com.shiffler.AcmeTestingCenter.web.mappers.MedicalTestOrderMapper;
import com.shiffler.AcmeTestingCenter.web.model.MedicalTestOrderDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.NoSuchElementException;
import java.util.Optional;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = MedicalTestOrderController.class)
class MedicalTestOrderControllerTest {

    @MockBean
    MedicalTestOrderService medicalTestOrderService;

    @MockBean
    MedicalTestOrderMapper medicalTestOrderMapper;

    @MockBean
    UserDetailsService userDetailsService; //Spring Security is looking for a bean of this type and will not
                                           //load without a userDetailsService available.

    @MockBean
    UserService userService;


    @Autowired
    MockMvc mockMvc;

    MedicalTestOrder medicalTestOrder;
    MedicalTestOrderDto medicalTestOrderDto;
    MedicalTestOrderDto medicalTestOrderDto1;

    @BeforeEach
    void init(){

        //Initialize Objects that will be used as part of our testing

        medicalTestOrder = new MedicalTestOrder();
        medicalTestOrder.setId(1000000000L);
        medicalTestOrder.setTestCode("00000A0002");
        medicalTestOrder.setMedicalTestResultEnum(MedicalTestResultEnum.WAITING_FOR_RESULT);
        medicalTestOrder.setTestOrderStatusEnum(MedicalTestOrderStatusEnum.ORDER_PLACED);

        medicalTestOrderDto = new MedicalTestOrderDto();
        medicalTestOrderDto.setId(1000000000L);
        medicalTestOrderDto.setTestCode("00000A0002");
        medicalTestOrderDto.setMedicalTestResultEnum(MedicalTestResultEnum.WAITING_FOR_RESULT);
        medicalTestOrderDto.setTestOrderStatusEnum(MedicalTestOrderStatusEnum.ORDER_PLACED);

        medicalTestOrderDto1 = new MedicalTestOrderDto();
        medicalTestOrderDto1.setId(1000000000L);
        medicalTestOrderDto1.setTestCode("00000A0002");
        medicalTestOrderDto1.setMedicalTestResultEnum(MedicalTestResultEnum.WAITING_FOR_RESULT);
        medicalTestOrderDto1.setTestOrderStatusEnum(MedicalTestOrderStatusEnum.ORDER_PLACED);

    }

    @AfterEach
    void tearDown(){
        reset(medicalTestOrderService);
    }

    @Test
    //Need to add the id in, had trouble formatting correctly
    //The casting makes the formatting turnout correctly
    @WithMockUser(roles={"ADMIN"}) //Allows us to run the test with Spring Security
    @DisplayName("Test getMedicalTestOrderById - Element is found")
    void getMedicalTestOrderById() throws Exception {

        //Given

        given(medicalTestOrderService
                .getMedicalTestOrderById(anyLong()))
                .willReturn(Optional.of(medicalTestOrder));

        given(medicalTestOrderMapper
                .medicalTestOrderToMedicalTestOrderDto(medicalTestOrder))
                .willReturn(medicalTestOrderDto);

        //The casting of l to int,makes the formatting turnout correctly
        //need to figure out a workaround
        long longId = medicalTestOrderDto.getId();

      //When

      MvcResult result = mockMvc
              .perform(get("/api/v1/medicaltestorders/" + medicalTestOrder.getId()))
      //Then
              .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.id", is((int) longId)))
              .andExpect(jsonPath("$.testCode", is(medicalTestOrderDto.getTestCode())))
              .andExpect(jsonPath("$.testOrderStatus",
                      is(medicalTestOrderDto.getTestOrderStatusEnum().toString())))
              .andExpect(jsonPath("$.medicalTestResult",
                      is(medicalTestOrderDto.getMedicalTestResultEnum().toString())))
              .andExpect(status().isOk())
              .andReturn();
    }

    @Test
    @WithMockUser(roles={"ADMIN"}) //Allows us to run the test with Spring Security
    @DisplayName("Test getMedicalTestOrderByIdNotFound - Element isn't found")
    void getMedicalTestOrderByIdNotFound() throws Exception {
        given(medicalTestOrderService
                .getMedicalTestOrderById(anyLong()))
                .willThrow(new NoSuchElementException());

        MvcResult result = mockMvc.perform(get("/api/v1/medicaltestorders/" + medicalTestOrder.getId()))
                                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    @WithMockUser(roles={"ADMIN"}) //Allows us to run the test with Spring Security
    @DisplayName("Test saveNewMedicalTestOrder() - With a valid MedicalTestOrder")
    void saveNewMedicalTestOrder() throws Exception {

        //Given - We're mocking the mapper so we have to "map" manually
        given(medicalTestOrderMapper
              .medicalTestOrderDtoToMedicalTestOrder(any()))
                    .willReturn(medicalTestOrder);
        given(medicalTestOrderService.saveMedicalTestOrder(medicalTestOrder)).willReturn(medicalTestOrder);

        //When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/medicaltestorders")

        //Then
                 //Map the POJO to a JSON Format
                .content(new ObjectMapper().writeValueAsString(medicalTestOrderDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        //Validate the location field tha we should get back that tells us the location of our new MedicalTestOrder
        String headervalue = result.getResponse().getHeader("Location");
        assertTrue(headervalue.equals("http://localhost:8081/api/v1/medicaltestorders/1000000000"));

    } //close method

} //close class