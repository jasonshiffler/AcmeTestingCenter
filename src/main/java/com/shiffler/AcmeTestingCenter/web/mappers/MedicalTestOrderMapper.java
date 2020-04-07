/*
Allows for conversion of our MedicalTestOrderDto that used in the Weblayer with our MedicalTestOrder that is used within the
repository
 */

package com.shiffler.AcmeTestingCenter.web.mappers;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
import com.shiffler.AcmeTestingCenter.web.model.MedicalTestOrderDto;
import org.mapstruct.Mapper;

@Mapper // Signals that this is a Mapstruct Mapper
public interface MedicalTestOrderMapper {

    MedicalTestOrderDto medicalTestOrderToMedicalTestOrderDto(MedicalTestOrder medicalTestOrder);

    MedicalTestOrder medicalTestOrderDtoToMedicalTestOrder(MedicalTestOrderDto medicalTestOrderDto);

    Iterable<MedicalTestOrderDto> medicalTestOrderIterableToMedicalTestOrderDtoIterable
            (Iterable<MedicalTestOrder> medicalTestOrder);

    Iterable<MedicalTestOrder> medicalTesOrderDtoIterableToMedicalTestOrderIterable
            (Iterable<MedicalTestOrderDto> medicalTestOrderDto);

}