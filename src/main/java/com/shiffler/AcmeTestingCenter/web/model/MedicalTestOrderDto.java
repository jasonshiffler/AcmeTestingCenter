package com.shiffler.AcmeTestingCenter.web.model;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrderStatusEnum;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestResultEnum;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrderStatusEnum;

public class MedicalTestOrderDto {

    private Long id;
    private String testCode;
    private MedicalTestOrderStatusEnum testOrderStatus;
    private MedicalTestResultEnum medicalTestResultEnum;
}
