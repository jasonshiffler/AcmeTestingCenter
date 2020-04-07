package com.shiffler.AcmeTestingCenter.repository;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrderStatusEnum;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MedicalTestOrderRepository extends CrudRepository<MedicalTestOrder, UUID> {


    @Query("select m from MedicalTestOrder m where m.testOrderStatus = ?1")
    Iterable<MedicalTestOrder> findByOrderStatus(MedicalTestOrderStatusEnum medicalTestOrderStatusEnum);

}
