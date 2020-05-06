/*
Allows users to be retrieved from the database.
 */

package com.shiffler.AcmeTestingCenter.repository;

import com.shiffler.AcmeTestingCenter.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
