/*
Specifies how information on the different MedicalTests that are offered can be retrieved
 */

package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.MedicalTest;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface MedicalTestService {
    Optional<MedicalTest> getMedicalTestById(UUID uuid);
    void saveMedicalTest(MedicalTest medicalTest);
}
