package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;
import com.shiffler.AcmeTestingCenter.repository.MedicalTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MedicalTestServiceImpl implements MedicalTestService {

    private final MedicalTestRepository medicalTestRepository;

    @Autowired
    MedicalTestServiceImpl(MedicalTestRepository medicalTestRepository){
        this.medicalTestRepository = medicalTestRepository;
    }


    /**
     * Allows a specific type of Medical Test to be retrieved by its id
     * param id - The id of the test we're searching for.
     * @return - An Optional of a MedicalTest
     */
    @Override
    public Optional<MedicalTest> getMedicalTestById(UUID id) {

       Optional<MedicalTest> medicalTestOptional = medicalTestRepository.findById(id);
       return medicalTestOptional;
    }

    /**
     * Allows a new Medical Test to be saved.
     * @param medicalTest
     */
    @Override
    public void saveMedicalTest(MedicalTest medicalTest) {

        initializeMedicalTest(medicalTest);
        medicalTestRepository.save(medicalTest);
    }

    @Override
    public Iterable<MedicalTest> getAllMedicalTests() {
        return medicalTestRepository.findAll();
    }

    /**
     * Sets initial values if certain fields of the MedicalTest haven't been set
     * @param medicalTest
     */
    private void initializeMedicalTest(MedicalTest medicalTest){

        if (medicalTest.getQuantityToOrder() == null) {
            medicalTest.setQuantityToOrder(10);
        }

        if (medicalTest.getQuantityOnHand() == null) {
            medicalTest.setQuantityOnHand(10);
        }

        if (medicalTest.getMinOnHand() == null) {
            medicalTest.setMinOnHand(10);
        }


    } //close method


}
