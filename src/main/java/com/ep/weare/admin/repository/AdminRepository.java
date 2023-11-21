package com.ep.weare.admin.repository;

import com.ep.weare.user.entity.UserCheck;
import com.ep.weare.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<UserEntity,String> {

    List<UserEntity> findByUserCheck(UserCheck userCheck);
}
