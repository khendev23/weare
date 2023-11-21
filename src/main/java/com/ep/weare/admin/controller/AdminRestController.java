package com.ep.weare.admin.controller;

import com.ep.weare.admin.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AdminRestController {

    private AdminService adminService;

    @Autowired
    public AdminRestController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/admin/approveUser.do")
    public ResponseEntity<String> approveUser(@RequestParam String userId) {

        adminService.patchUserCheckByUserId(userId);


        return ResponseEntity.ok("승인되었습니다.");
    }

}
