package com.ep.weare.user.service;

import com.ep.weare.user.dto.UserSignupDTO;
import com.ep.weare.user.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService{

    UserEntity createUser(UserEntity userEntity);

    boolean isUserIdDuplicate(String userId);


    Optional<UserEntity> findById(String username);

}
