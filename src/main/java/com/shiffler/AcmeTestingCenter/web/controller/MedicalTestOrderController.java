/*
Allows new orders for medical tests to be placed as well
 */

package com.shiffler.AcmeTestingCenter.web.controller;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
import com.shiffler.AcmeTestingCenter.service.MedicalTestOrderService;
import com.shiffler.AcmeTestingCenter.web.mappers.MedicalTestOrderMapper;
import com.shiffler.AcmeTestingCenter.web.model.MedicalTestOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/medicaltestorder")
public class MedicalTestOrderController {

    private final MedicalTestOrderService medicalTestOrderService;
    private final MedicalTestOrderMapper medicalTestOrderMapper;

    @Autowired
    public MedicalTestOrderController(MedicalTestOrderService medicalTestOrderService,
                                      MedicalTestOrderMapper medicalTestOrderMapper) {
        this.medicalTestOrderService = medicalTestOrderService;
        this.medicalTestOrderMapper = medicalTestOrderMapper;
    }

    /**
     * Allows a MedicalTest Order to be retrieved by the id. Returns a Dto object to hide fields that the end user
     * doesn't need to see.
     * @param id - the id of the MedicalTestOrder that is being retrieved
     * @return - The Medical Test Order in JSON format
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicalTestOrderDto> getMedicalTestOrderById(@PathVariable("id") UUID id){

        Optional<MedicalTestOrder> optionalMedicalTestOrder = medicalTestOrderService.getMedicalTestOrderById(id);

        MedicalTestOrderDto medicalTestOrderDto = medicalTestOrderMapper
                .medicalTestOrderToMedicalTestOrderDto(optionalMedicalTestOrder.get());


        return new ResponseEntity<MedicalTestOrderDto>(medicalTestOrderDto, HttpStatus.OK);
    }

    /**
     * Allows requests to be made for new Medical Test Orders
     * @param medicalTestOrderDto - The MedicalTestOrder that is being submitted.
     * @return A Response entity with the Location Field set to the location of the new MedicalTestOrder
     * @throws URISyntaxException
     */
    @PostMapping
    public ResponseEntity saveNewMedicalTestOrder(@RequestBody @Valid MedicalTestOrderDto medicalTestOrderDto)
            throws URISyntaxException {

        MedicalTestOrder medicalTestOrder = medicalTestOrderMapper
                .medicalTestOrderDtoToMedicalTestOrder(medicalTestOrderDto);

        try {
            medicalTestOrderService.saveMedicalTestOrder(medicalTestOrder);
        } catch (Exception e)
        {

        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("http://localhost:8081/" + medicalTestOrder.getId()));
        return new ResponseEntity(headers,HttpStatus.CREATED);
    }


}
