package com.ep.weare.admin.repository;

import com.ep.weare.user.entity.UserCheck;
import com.ep.weare.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<UserEntity,String> {

    List<UserEntity> findByUserCheck(UserCheck userCheck);
}
