/*
This DTO object presents only the fields the enduser needs to see for a particular MedicalTestOrder as there is some
internal information that doesn't need to be exposed.
 */

package com.shiffler.AcmeTestingCenter.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrderStatusEnum;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestResultEnum;

import lombok.Data;

@Data//This is required for Mapstruct to work properly
public class MedicalTestOrderDto {

    //This annotation allows this value to be Serialized but never deserialized as we
    //don't want users to be able to modify this value
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String testCode;

    //value allows us to present this data as a different key, we don't need to let our users
    //know this is an Enum value underneath.
    @JsonProperty(value="testOrderStatus", access = JsonProperty.Access.READ_ONLY)
    private MedicalTestOrderStatusEnum testOrderStatusEnum;

    @JsonProperty(value="medicalTestResult", access = JsonProperty.Access.READ_ONLY)
    private MedicalTestResultEnum medicalTestResultEnum;
}
