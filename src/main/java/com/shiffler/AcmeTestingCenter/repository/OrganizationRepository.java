/*
Allows different organizations to be accessed from the database
 */

package com.shiffler.AcmeTestingCenter.repository;

import com.shiffler.AcmeTestingCenter.entity.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, UUID> {

    Optional<Organization> findByOrganizationName(String organizationName);
}
