package com.sasha.authenticationservice.service.repository;

import com.sasha.authenticationservice.service.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findOneByName(String name);
}
