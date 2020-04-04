package com.shiffler.AcmeTestingCenter.web.controller;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.service.MedicalTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/medicaltest")
@Slf4j
public class MedicalTestController {

    private final MedicalTestService medicalTestService;

    @Autowired
    public MedicalTestController(MedicalTestService medicalTestService) {
        this.medicalTestService = medicalTestService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalTest> getMedicalTestById(@PathVariable("id")UUID id){

        Optional<MedicalTest> optionalMedicalTest = medicalTestService.getMedicalTestById(id);
        return new ResponseEntity<MedicalTest>(optionalMedicalTest.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity saveNewMedicalTest(@RequestBody @Valid MedicalTest medicalTest){

        log.info("hello");
        HttpHeaders headers = new HttpHeaders();
        medicalTestService.saveMedicalTest(medicalTest);
        headers.add("Location:", "http://hostname/api/v1/medicaltest/" + medicalTest.toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }


}
