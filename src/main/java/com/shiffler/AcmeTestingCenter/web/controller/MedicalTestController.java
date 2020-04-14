/*
Provides a REST API interface to the different types of Medical Tests that are available to order
 */

package com.shiffler.AcmeTestingCenter.web.controller;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.service.MedicalTestService;
import com.shiffler.AcmeTestingCenter.web.mappers.MedicalTestMapper;
import com.shiffler.AcmeTestingCenter.web.model.MedicalTestDto;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/v1/medicaltests")
@Slf4j
public class MedicalTestController {

    private final MedicalTestService medicalTestService;
    private final MedicalTestMapper medicalTestMapper;

    @Autowired
    public MedicalTestController(MedicalTestService medicalTestService, MedicalTestMapper medicalTestMapper) {
        this.medicalTestService = medicalTestService;
        this.medicalTestMapper = medicalTestMapper;
    }

    /**
     * Allows a specific medical test type to be retrieved by its id
     * @param id The id of the MedicalTestDto we're searching for
     * @return The MedicalTestDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicalTestDto> getMedicalTestById(@PathVariable("id")UUID id){

        log.info("Web layer request to search for a MedicalTest by id: {}", id);
        Optional<MedicalTest> optionalMedicalTest = medicalTestService.getMedicalTestById(id);
        MedicalTestDto medicalTestDto = medicalTestMapper.medicalTestToMedicalTestDto(optionalMedicalTest.get());
        return new ResponseEntity<MedicalTestDto>(medicalTestDto, HttpStatus.OK);

    } //close method


    /**
     * Returns all of the available Medical Tests
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<Iterable<MedicalTestDto>> getAllMedicalTests(){

        log.info("Web layer request to search for all available Medical tests");
        Iterable<MedicalTest> medicalTestIterable = medicalTestService.getAllMedicalTests();
        Iterable<MedicalTestDto> medicalTestDtoIterable = medicalTestMapper
                .medicalTestIterableToMedicalTestDtoIterable(medicalTestIterable);

        return new ResponseEntity<Iterable<MedicalTestDto>>(medicalTestDtoIterable, HttpStatus.OK);

    } //close method

    /**
     * Allows a new MedicalTest type to be saved. The @Valid annotation in the method header allows us to control
     * the values that are populated upon medical test creation. For example we do not want users to be able to populate
     * the id field.
     * @param medicalTestDto - The MedicalTestDto object that will be converted and saved to the database.
     * @return
     */
    @PostMapping
    public ResponseEntity saveNewMedicalTest(@RequestBody @Valid MedicalTestDto medicalTestDto) throws URISyntaxException {

        log.info("Web layer request to save new MedicalTest {}", medicalTestDto.toString());

        //Convert the Dto Object to the Entity format
        MedicalTest medicalTest = medicalTestMapper.medicalTestDtoToMedicalTest(medicalTestDto);

        //Call the service layer to save the new MedicalTest
        medicalTestService.saveMedicalTest(medicalTest);

        //Send a response
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("http://localhost:8081/api/v1/medicaltests" + medicalTest.getId()));

        return new ResponseEntity(headers,HttpStatus.CREATED);

    } //close method


}
