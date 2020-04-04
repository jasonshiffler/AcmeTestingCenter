/*
Allows for conversion of our MedicalTestDto that used in the Weblayer with our MedicalTest that is used within the
repository
 */

package com.shiffler.AcmeTestingCenter.web.mappers;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.web.model.MedicalTestDto;
import org.mapstruct.Mapper;

@Mapper // Signals that this is a Mapstruct Mapper
public interface MedicalTestMapper {

    MedicalTestDto medicalTestToMedicalTestDto(MedicalTest medicalTest);

    MedicalTest medicalTestDtoToMedicalTest (MedicalTestDto medicalTestDto);
}
