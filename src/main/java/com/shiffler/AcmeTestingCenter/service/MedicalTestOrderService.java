/*
Specifies actions available for the different MedicalTestOrders
 */

package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MedicalTestOrderService {

    Optional<MedicalTestOrder> getMedicalTestOrderById(UUID id);
    MedicalTestOrder saveMedicalTestOrder(MedicalTestOrder medicalTestOrder);
    List<MedicalTestOrder> updateOrderStatusOnHoldToTestInProcess(String testCode, int num);
}
