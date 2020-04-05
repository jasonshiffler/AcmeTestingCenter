package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public interface MedicalTestOrderService {

    Optional<MedicalTestOrder> getMedicalTestOrderById(UUID id);
    MedicalTestOrder saveMedicalTestOrder(MedicalTestOrder medicalTestOrder) throws NoSuchElementException;
}
