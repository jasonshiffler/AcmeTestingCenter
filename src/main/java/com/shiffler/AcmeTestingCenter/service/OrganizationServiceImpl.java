package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.Organization;
import com.shiffler.AcmeTestingCenter.repository.OrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class OrganizationServiceImpl implements OrganizationService{

    private final OrganizationRepository organizationRepository;

    /**
     * Object Constructor
     * @param organizationRepository
     */
    @Autowired
    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    /**
     * Allows a search for an organization by id
     * @param id - the id that is being searched for
     * @return - The result of the search
     */
    @Override
    public Optional<Organization> getOrganizationById(UUID id) {
      log.info("Searching for Organization with id: " + id.toString());
        return organizationRepository.findById(id);
    }

    /**
     * Search for an organization by organizationName
     * @param organizationName - A String representing the organization name
     * @return - the result of the search
     */
    @Override
    public Optional<Organization> getOrganizationByOrganizationName(String organizationName) {
        log.info("Searching for Organization with name: " + organizationName.toString());
        return organizationRepository.findByOrganizationName(organizationName);
    }

    /**
     * Persist an organization to the database
     * @param organization - the organization to be persisted
     * @return
     */
    @Override
    public Organization saveOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }
}
