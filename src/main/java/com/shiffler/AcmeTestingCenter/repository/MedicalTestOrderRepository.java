package com.shiffler.AcmeTestingCenter.repository;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrderStatusEnum;
import com.shiffler.AcmeTestingCenter.entity.Organization;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MedicalTestOrderRepository extends CrudRepository<MedicalTestOrder, Long> {


    //Find a Medical Test Order with a particular order status
    @Query("select m from MedicalTestOrder m where m.testOrderStatusEnum = ?1")
    List<MedicalTestOrder> findByOrderStatus(MedicalTestOrderStatusEnum medicalTestOrderStatusEnum);

    //Retrieve the count of all MedicalTestsOrders with a certain status.
    long countByTestOrderStatusEnum(MedicalTestOrderStatusEnum medicalTestOrderStatusEnum);

    //Retrieve the count of MedicalTestsOrders with a certain status and testCode.
    long countByTestOrderStatusEnumAndTestCode(MedicalTestOrderStatusEnum medicalTestOrderStatusEnum,
                                               String testCode);

    //Find a test order with a matching id and organization
    Optional<MedicalTestOrder> findByIdAndOrganization(Long id, Organization organization);
}
