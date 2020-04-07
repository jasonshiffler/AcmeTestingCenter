/*
This DTO object presents only the fields the enduser needs to see for a particular MedicalTest as there is some
internal information that doesn't need to be exposed.
 */

package com.shiffler.AcmeTestingCenter.web.model;

import lombok.Data;

import javax.validation.constraints.Null;
import java.util.UUID;

@Data //This is required for Mapstruct to work properly
public class MedicalTestDto {

    @Null //Allows us to use the @Valid Annotation on the POST Mapping so someone can't set the id
    private UUID id;

    private String testName;

    @Null //Don't allow anyone using the API to change the quantity
    private Integer quantityOnHand;

    private String testCode;
}
