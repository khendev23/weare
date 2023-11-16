package com.ep.weare.user.repository;

import com.ep.weare.user.entity.MemberDetails;
import com.ep.weare.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {

    boolean existsByUserId(String userId);

}