package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.Organization;
import com.shiffler.AcmeTestingCenter.entity.User;
import com.shiffler.AcmeTestingCenter.repository.MedicalTestRepository;
import com.shiffler.AcmeTestingCenter.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    Organization organization;

    User user;

    @BeforeEach
    void init(){
        organization = new Organization();
        organization.setOrganizationName("General Hospital");
        user = new User();
        user.setUsername("jsmith");
        user.setOrganization(organization);

    }


    @Test
    void getOrgByUsername() {

        //Given
        given(userRepository.findByUsername("jsmith")).willReturn(user);

        //When
        Organization testOrg = userService.getOrgByUsername("jsmith");

        //Then
        assertTrue(testOrg.getOrganizationName().equals("General Hospital"));
    }
}