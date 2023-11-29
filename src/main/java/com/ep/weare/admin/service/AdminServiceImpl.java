package com.ep.weare.admin.service;

import com.ep.weare.admin.entity.Kelly;
import com.ep.weare.admin.repository.AdminRepository;
import com.ep.weare.admin.repository.KellyRepository;
import com.ep.weare.user.entity.UserCheck;
import com.ep.weare.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private AdminRepository adminRepository;
    private KellyRepository kellyRepository;

    public AdminServiceImpl(AdminRepository adminRepository, KellyRepository kellyRepository) {
        this.adminRepository = adminRepository;
        this.kellyRepository = kellyRepository;
    }

    @Override
    @Transactional
    public void patchUserCheckByUserId(String userId) {

        Optional<UserEntity> userOptional = adminRepository.findById(userId);

        // Optional.ifPresent를 사용하여 값이 존재할 때 로직 수행
        userOptional.ifPresent(userEntity -> {
            userEntity.setUserCheck(UserCheck.o);
            adminRepository.save(userEntity); // userEntity를 save 메소드에 직접 전달
        });

    }

    @Override
    public List<UserEntity> findByUserCheck(UserCheck userCheck) {
        return adminRepository.findByUserCheck(userCheck);
    }

    @Override
    public void insertKelly(Kelly attach) {
        kellyRepository.save(attach);
    }

    @Override
    public Optional<Kelly> findFirstByOrderByKellyIdDesc() {
        return kellyRepository.findFirstByOrderByKellyIdDesc();
    }

    @Override
    public List<Kelly> findTop3ByOrderByKellyIdDesc() {
        return kellyRepository.findTop3ByOrderByKellyIdDesc();
    }

    @Override
    public List<Kelly> findAllByOrderByKellyIdDesc() {
        return kellyRepository.findAllByOrderByKellyIdDesc();
    }


}
