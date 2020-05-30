/*
Allows new orders for medical tests to be placed as well
 */

package com.shiffler.AcmeTestingCenter.web.controller;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
import com.shiffler.AcmeTestingCenter.entity.Organization;
import com.shiffler.AcmeTestingCenter.repository.UserRepository;
import com.shiffler.AcmeTestingCenter.service.MedicalTestOrderService;
import com.shiffler.AcmeTestingCenter.service.UserService;
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
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/medicaltestorders")
public class MedicalTestOrderController {

    private final MedicalTestOrderService medicalTestOrderService;
    private final MedicalTestOrderMapper medicalTestOrderMapper;
    private final UserService userService;

    @Autowired
    public MedicalTestOrderController(MedicalTestOrderService medicalTestOrderService,
                                      MedicalTestOrderMapper medicalTestOrderMapper, UserService userService) {
        this.medicalTestOrderService = medicalTestOrderService;
        this.medicalTestOrderMapper = medicalTestOrderMapper;
        this.userService = userService;
    }


    /**
     * Allows a MedicalTest Order to be retrieved by the id. Returns a Dto object to hide fields that the end user
     * doesn't need to see.
     * @param id - the id of the MedicalTestOrder that is being retrieved
     * @param principal - the principle associated with the request
     * @return - The Medical Test Order in JSON format
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicalTestOrderDto> getMedicalTestOrderById(@PathVariable("id") Long id,Principal principal){

        //Determine the organization the user is associated with
        Organization organization = userService.getOrgByUsername(principal.getName());

        Optional<MedicalTestOrder> optionalMedicalTestOrder = medicalTestOrderService
                .getMedicalTestOrderByIdAndOrganization(id, organization);

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
    public ResponseEntity saveNewMedicalTestOrder(@RequestBody @Valid MedicalTestOrderDto medicalTestOrderDto,
                                                  Principal principal) throws URISyntaxException {

        MedicalTestOrder medicalTestOrder = medicalTestOrderMapper
                .medicalTestOrderDtoToMedicalTestOrder(medicalTestOrderDto);

        //Set the organization the order is associated with based on the authenticated user that is submitting it.
        medicalTestOrder.setOrganization(userService.getOrgByUsername(principal.getName()));

        try {
            medicalTestOrderService.saveMedicalTestOrder(medicalTestOrder);
        } catch (Exception e) {

        }

        //We're returning a response that contains the location of the created object
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("http://localhost:8081/api/v1/medicaltestorders/" + medicalTestOrder.getId()));
        return new ResponseEntity(headers,HttpStatus.CREATED);
    }


}
