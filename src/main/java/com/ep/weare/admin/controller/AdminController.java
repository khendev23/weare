package com.ep.weare.admin.controller;

import com.ep.weare.admin.service.AdminService;
import com.ep.weare.user.entity.UserCheck;
import com.ep.weare.user.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@Slf4j
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin")
    public String getAdminPage(Model model) {

        List<UserEntity> unCheckedUserList = adminService.findByUserCheck(UserCheck.x);
        List<UserEntity> checkedUserList = adminService.findByUserCheck(UserCheck.o);

        model.addAttribute("unCheckedUserList", unCheckedUserList);
        model.addAttribute("checkedUserList", checkedUserList);

        return "admin/adminPage";
    }


}
