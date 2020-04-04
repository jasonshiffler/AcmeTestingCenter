/*
Allows new orders for medical tests to be placed as well
 */

package com.shiffler.AcmeTestingCenter.web.controller;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
import com.shiffler.AcmeTestingCenter.service.MedicalTestOrderService;
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

    @Autowired
    public MedicalTestOrderController(MedicalTestOrderService medicalTestOrderService) {
        this.medicalTestOrderService = medicalTestOrderService;
    }

    /**
     * Allows a MedicalTest Order to be retrieved by the id
     * @param id - the id of the MedicalTestOrder that is being retrieved
     * @return - The Medical Test Order in JSON format
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicalTestOrder> getMedicalTestOrderById(@PathVariable("id") UUID id){

        Optional<MedicalTestOrder> optionalMedicalTestOrder = medicalTestOrderService.getMedicalTestOrderById(id);
        return new ResponseEntity<MedicalTestOrder>(optionalMedicalTestOrder.get(), HttpStatus.OK);
    }

    /**
     * Allows requests to be made for new Medical Tests
     * @param medicalTestOrder
     * @return A Response entity with the Location Field set to the location of the new MedicalTestOrder
     * @throws URISyntaxException
     */
    @PostMapping
    public ResponseEntity saveNewMedicalTest(@RequestBody @Valid MedicalTestOrder medicalTestOrder) throws URISyntaxException {

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
