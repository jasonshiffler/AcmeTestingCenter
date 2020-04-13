package com.shiffler.AcmeTestingCenter.web.controller;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrderStatusEnum;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestResultEnum;
import com.shiffler.AcmeTestingCenter.service.MedicalTestOrderService;
import com.shiffler.AcmeTestingCenter.web.mappers.MedicalTestMapper;
import com.shiffler.AcmeTestingCenter.web.mappers.MedicalTestOrderMapper;
import com.shiffler.AcmeTestingCenter.web.mappers.MedicalTestOrderMapperImpl;
import com.shiffler.AcmeTestingCenter.web.model.MedicalTestOrderDto;
import javafx.util.converter.BigDecimalStringConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;

@WebMvcTest(MedicalTestOrderController.class)
class MedicalTestOrderControllerTest {

    @MockBean
    MedicalTestOrderService medicalTestOrderService;

    @MockBean
    MedicalTestOrderMapper medicalTestOrderMapper;

    @Autowired
    MockMvc mockMvc;

    MedicalTestOrder medicalTestOrder;
    MedicalTestOrderDto medicalTestOrderDto;

    @BeforeEach
    void init(){

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
    }

    @AfterEach
    void tearDown(){
        reset(medicalTestOrderService);
    }


    @Test
    //Need to add the id in, had trouble formatting correctly
    //The casting makes the formatting turnout correctly
    @DisplayName("Test getMedicalTestOrderById - Element is found")
    void getMedicalTestOrderById() throws Exception {

        //Given

        given(medicalTestOrderService
                .getMedicalTestOrderById(anyLong()))
                .willReturn(Optional.of(medicalTestOrder));

        given(medicalTestOrderMapper
                .medicalTestOrderToMedicalTestOrderDto(Optional.of(medicalTestOrder)
                        .get())).willReturn(medicalTestOrderDto);


        //The casting of l to int,makes the formatting turnout correctly
        //need to figure out a workaround
        long longId = medicalTestOrderDto.getId();

      //When

      MvcResult result = mockMvc
              .perform(get("/api/v1/medicaltestorder/" + medicalTestOrder.getId()))

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
    @DisplayName("Test getMedicalTestOrderByIdNotFound - Element isn't found")
    void getMedicalTestOrderByIdNotFound() throws Exception {
        given(medicalTestOrderService
                .getMedicalTestOrderById(anyLong()))
                .willThrow(new NoSuchElementException());

        MvcResult result = mockMvc.perform(get("/api/v1/medicaltestorder/" + medicalTestOrder.getId()))
                                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    void saveNewMedicalTestOrder() {
    }
}