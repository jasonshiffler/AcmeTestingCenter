package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.Organization;

public interface UserService {

    Organization getOrgByUsername(String username);
}
