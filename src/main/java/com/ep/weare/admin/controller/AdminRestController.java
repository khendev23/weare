package com.ep.weare.admin.controller;

import com.ep.weare.admin.service.AdminService;
import com.ep.weare.user.entity.UserEntity;
import com.ep.weare.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
public class AdminRestController {

    private final AdminService adminService;
    private final UserService userService;

    @Autowired
    public AdminRestController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @PostMapping("/admin/approveUser.do")
    public ResponseEntity<String> approveUser(@RequestParam String userId) {

        log.info("userId : {}", userId);
        adminService.patchUserCheckByUserId(userId);


        return ResponseEntity.ok("승인되었습니다.");
    }

    @GetMapping("/admin/userInfo.do")
    public UserEntity getUserInfo(@RequestParam String userId) {

        Optional<UserEntity> userEntityOptional = adminService.findByUserId(userId);


        return userEntityOptional.get();
    }

}
