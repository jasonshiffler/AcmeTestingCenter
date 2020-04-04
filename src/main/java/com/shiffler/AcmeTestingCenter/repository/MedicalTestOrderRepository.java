package com.shiffler.AcmeTestingCenter.repository;

import com.shiffler.AcmeTestingCenter.entity.MedicalTestOrder;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MedicalTestOrderRepository extends CrudRepository<MedicalTestOrder, UUID> {
}
