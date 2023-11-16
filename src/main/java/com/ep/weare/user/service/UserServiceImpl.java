package com.ep.weare.user.service;

import com.ep.weare.user.entity.Authority;
import com.ep.weare.user.entity.UserEntity;
import com.ep.weare.user.repository.AuthorityRepository;
import com.ep.weare.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthorityRepository authorityRepository) {

        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public UserEntity createUser(UserEntity userEntity) {

        String userId = userEntity.getUserId();

        Authority authority = new Authority();
        authority.setUserId(userId);
        authority.setAuthority("user");

        userRepository.save(userEntity);

        authorityRepository.save(authority);

        return userEntity;
    }

    @Override
    public boolean isUserIdDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }

    @Override
    public Optional<UserEntity> findById(String username) {
        return userRepository.findById(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> findOne = userRepository.findById(username);
        if (findOne.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        UserEntity user = findOne.get();

        log.info("User details - Username: {}, Password: {}, Roles: {}",
                user.getUserId(), user.getUserPw(), user.getAuthority().getAuthority());

        return User.builder()
                .username(user.getUserId())
                .password(user.getUserPw())
                .roles(user.getAuthority().getAuthority())
                .build();
    }
}
