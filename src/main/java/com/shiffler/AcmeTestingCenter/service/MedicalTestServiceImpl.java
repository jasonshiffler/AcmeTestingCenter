package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.repository.MedicalTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.UUID;

@Service
public class MedicalTestServiceImpl implements MedicalTestService {

    MedicalTestRepository medicalTestRepository;

    @Autowired
    MedicalTestServiceImpl(MedicalTestRepository medicalTestRepository){
        this.medicalTestRepository = medicalTestRepository;
    }


    @Override
    public Optional<MedicalTest> getMedicalTestById(UUID uuid) {

       Optional<MedicalTest> medicalTestOptional = medicalTestRepository.findById(uuid);
       return medicalTestOptional;
    }

    @Override
    public void saveMedicalTest(MedicalTest medicalTest) {
        medicalTestRepository.save(medicalTest);

    }
}
