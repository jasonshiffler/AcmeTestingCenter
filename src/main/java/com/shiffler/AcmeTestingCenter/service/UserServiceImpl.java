package com.shiffler.AcmeTestingCenter.service;

import com.shiffler.AcmeTestingCenter.entity.Organization;
import com.shiffler.AcmeTestingCenter.entity.User;
import com.shiffler.AcmeTestingCenter.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns the organization associated with a specific username
     *
     * @param username - The username we're searching on.
     * @return - The organization that a particular user belongs to.
     */
    @Override
    public Organization getOrgByUsername(String username) {
        log.debug("Searching for organization associated with: " + username);
         User user = userRepository.findByUsername(username);
         return user.getOrganization();
    }
}
