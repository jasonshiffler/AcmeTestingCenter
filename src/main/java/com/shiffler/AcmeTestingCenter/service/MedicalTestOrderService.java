/*
Specifies actions available for the different MedicalTestOrders
 */

package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrderStatusEnum;
import com.shiffler.AcmeTestingCenter.entity.Organization;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

public interface MedicalTestOrderService {

    Optional<MedicalTestOrder> getMedicalTestOrderByIdAndOrganization(Long id, Organization organization);
    MedicalTestOrder saveMedicalTestOrder(MedicalTestOrder medicalTestOrder);
    void processMedicalTestOrders();
    void processTests();
    long getTestCountByStatus(MedicalTestOrderStatusEnum medicalTestOrderStatusEnum);
    long getTestCountByTestCodeAndStatus(MedicalTestOrderStatusEnum medicalTestOrderStatusEnum, String testCode);
}
