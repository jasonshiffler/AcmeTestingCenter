package com.shiffler.AcmeTestingCenter.repository;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrderStatusEnum;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface MedicalTestOrderRepository extends CrudRepository<MedicalTestOrder, Long> {


    //Find a Medical Test Order with a particular order status
    @Query("select m from MedicalTestOrder m where m.testOrderStatusEnum = ?1")
    List<MedicalTestOrder> findByOrderStatus(MedicalTestOrderStatusEnum medicalTestOrderStatusEnum);

}
