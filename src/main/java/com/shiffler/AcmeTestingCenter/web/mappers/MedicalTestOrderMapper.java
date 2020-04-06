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

    /**
     * Converts a MedicalTestOrder to a MedicalTestOrderDto
     * @param medicalTestOrder the MedicalTestOrder that needs to be converted
     * @return The equivalent MedicalTestOrderDto
     */
    MedicalTestOrderDto medicalTestOrderToMedicalTestOrderDto(MedicalTestOrder medicalTestOrder);

    /**
     * Converts a MedicalTestOrderDto to a MedicalTestOrder
     * @param medicalTestOrderDto the MedicalTestOrder that needs to be converted
     * @return The equivalent MedicalTestOrder
     */
    MedicalTestOrder medicalTestOrderDtoToMedicalTestOrder (MedicalTestOrderDto medicalTestOrderDto);

    /**
     * Converts an Iterable of MedicalTestOrders to an Iterable of MedicalTestOrderDtos
     * @param medicalTestOrder - The Iterable of MedicalTestOrders
     * @return an Iterable of MedicalTestOrderDtos
     */
    Iterable<MedicalTestOrderDto> medicalTestOrderIterableToMedicalTestOrderDtoIterable
            (Iterable<MedicalTestOrder> medicalTestOrder);

    /**
     * Converts an Iterable of MedicalTestOrderDtos to an Iterable of MedicalTestOrders
     * @param medicalTestOrderDto The Iterable of MedicalTestOrderDtos that needs to be converted
     * @return an Iterable of MedicalTestOrders
     */
    Iterable<MedicalTestOrder> medicalTesOrderDtoIterableToMedicalTestOrderIterable
            (Iterable<MedicalTestOrderDto> medicalTestOrderDto);

}