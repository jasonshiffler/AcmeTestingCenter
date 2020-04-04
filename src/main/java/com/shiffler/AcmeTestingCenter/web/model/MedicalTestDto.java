package com.shiffler.AcmeTestingCenter.web.model;


import java.time.OffsetDateTime;
import java.util.UUID;

public class MedicalTestDto {

    private UUID id;
    private OffsetDateTime createdDateTime;
    private OffsetDateTime lastModifiedDateTime;
    private String testName;
    private Integer quantityOnHand;

    private String testCode;


}
