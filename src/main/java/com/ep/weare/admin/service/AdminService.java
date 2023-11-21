package com.ep.weare.admin.service;

import com.ep.weare.user.entity.UserCheck;
import com.ep.weare.user.entity.UserEntity;

import java.util.List;

public interface AdminService {

    void patchUserCheckByUserId(String userId);

    List<UserEntity> findByUserCheck(UserCheck userCheck);
}
