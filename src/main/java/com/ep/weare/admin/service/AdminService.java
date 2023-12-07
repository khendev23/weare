package com.ep.weare.admin.service;

import com.ep.weare.admin.entity.Kelly;
import com.ep.weare.user.entity.*;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    void patchUserCheckByUserId(String userId);

    List<UserEntity> findByUserCheck(UserCheck userCheck);

    void insertKelly(Kelly attach);

    Optional<Kelly> findFirstByOrderByKellyIdDesc();

    List<Kelly> findTop3ByOrderByKellyIdDesc();

    List<Kelly> findAllByOrderByKellyIdDesc();

    Optional<UserEntity> findByUserId(String userId);

    List<Team> findTeamAll();

    List<ExecutivesRank> findRankAll();

    Optional<Authority> findAuthorityByUserId(String userId);

    void saveAuthority(Authority authority);

    void saveUser(UserEntity userEntity);
}
