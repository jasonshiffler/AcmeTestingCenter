/*
Specifies actions available for the different MedicalTestOrders
 */

package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrderStatusEnum;

import java.util.Optional;
import java.util.UUID;

public interface MedicalTestOrderService {

    Optional<MedicalTestOrder> getMedicalTestOrderById(Long id);
    MedicalTestOrder saveMedicalTestOrder(MedicalTestOrder medicalTestOrder);
    void processMedicalTestOrders();
    void processTests();
    long getTestCountByStatus(MedicalTestOrderStatusEnum medicalTestOrderStatusEnum);
    long getTestCountByTestCodeAndStatus(MedicalTestOrderStatusEnum medicalTestOrderStatusEnum, String testCode);
}
