package com.shiffler.AcmeTestingCenter.web.model;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestResultEnum;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestStatusEnum;

public class MedicalTestOrderDto {

    private Long id;
    private String testCode;
    private MedicalTestStatusEnum testStatus;
    private MedicalTestResultEnum medicalTestResultEnum;
}
