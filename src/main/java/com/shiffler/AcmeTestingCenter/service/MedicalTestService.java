/*
Specifies actions available for the different MedicalTests
 */

package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;

import java.util.Optional;
import java.util.UUID;

public interface MedicalTestService {

    Optional<MedicalTest> getMedicalTestById(UUID uuid);
    void saveMedicalTest(MedicalTest medicalTest);
    boolean verifyTestCode(String testCode);
    Iterable<MedicalTest> getAllMedicalTests();
    Optional<MedicalTest> getMedicalTestByTestCode(String testCode);
    void logLowInventoryMedicalTests();
    void addStockToMedicalTests();

}
