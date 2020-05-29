/*
This class provides for operations on Organization Objects
 */

package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.Organization;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationService {

    Optional<Organization> getOrganizationById(UUID id);
    Optional<Organization> getOrganizationByOrganizationName(String organizationName);
    Organization saveOrganization(Organization organization);


}
