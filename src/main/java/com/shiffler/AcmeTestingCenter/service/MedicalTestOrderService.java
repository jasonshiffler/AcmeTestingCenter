package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.exceptions.ItemNotFoundException;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;

import java.util.Optional;
import java.util.UUID;

public interface MedicalTestOrderService {

    Optional<MedicalTestOrder> getMedicalTestOrderById(UUID id);
    MedicalTestOrder saveMedicalTestOrder(MedicalTestOrder medicalTestOrder) throws ItemNotFoundException;
}
