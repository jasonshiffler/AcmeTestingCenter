package com.shiffler.AcmeTestingCenter.web.model;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrderStatusEnum;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestResultEnum;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrderStatusEnum;
import lombok.Data;

@Data//This is required for Mapstruct to work properly
public class MedicalTestOrderDto {

    private Long id;
    private String testCode;
    private MedicalTestOrderStatusEnum testOrderStatus;
    private MedicalTestResultEnum medicalTestResultEnum;
}
