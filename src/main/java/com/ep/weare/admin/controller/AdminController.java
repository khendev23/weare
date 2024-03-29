package com.ep.weare.admin.controller;

import com.ep.weare.admin.entity.Kelly;
import com.ep.weare.admin.service.AdminService;
import com.ep.weare.common.Utils;
import com.ep.weare.ministry.service.MinistryService;
import com.ep.weare.user.entity.*;
import com.ep.weare.user.service.UserService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final MinistryService ministryService;

    @Autowired
    public AdminController(AdminService adminService, MinistryService ministryService) {
        this.adminService = adminService;
        this.ministryService = ministryService;
    }

    // 켈리 파일 저장 디렉토리
    @Value("${file.upload.directory.kelly:tomcat/webapps/kelly}")
    String uploadDirectory;

    // 어드민 페이지 이동
    @GetMapping("/admin")
    public String getAdminPage(Model model) {

        List<UserEntity> unCheckedUserList = adminService.findByUserCheck(UserCheck.x);
        List<UserEntity> checkedUserList = adminService.findByUserCheck(UserCheck.o);

        // 사역팀 목록 전달
        List<MinistryTeam> ministryTeams = ministryService.findAll();

        // 조 목록 전달
        List<Team> teams = adminService.findTeamAll();

        // 직급 목록 전달
        List<ExecutivesRank> executivesRanks = adminService.findRankAll();

        model.addAttribute("executivesRanks", executivesRanks);
        model.addAttribute("teams", teams);
        model.addAttribute("ministryTeams", ministryTeams);
        model.addAttribute("userEntity", new UserEntity());
        model.addAttribute("unCheckedUserList", unCheckedUserList);
        model.addAttribute("checkedUserList", checkedUserList);

        return "admin/adminPage";
    }

    // 켈리 관리 페이지 이동
    @GetMapping("/kellyManage")
    public String getKellyManagePage(Model model) {

        // Flash 속성에서 "msg" 키로 추가한 값을 가져옴
        Object flashMessage = model.asMap().get("msg");

        if (flashMessage != null) {
            // 가져온 메시지를 모델에 추가
            model.addAttribute("flashMessage", flashMessage);
        }

        List<Kelly> getAllKellys = adminService.findAllByOrderByKellyIdDesc();

        List<List<Kelly>> chunkedKellys = Lists.partition(getAllKellys, 2);

        model.addAttribute("kellys", chunkedKellys);

        return "admin/kellyManage";
    }

    // 켈리 추가
    @PostMapping("kellyCreate.do")
    public String kellyCreate (Model model,
                               RedirectAttributes redirectAttr,
                               @RequestParam String bibleVerse,
                               @RequestParam(value = "kellyFile", required = false) List<MultipartFile> files) throws IOException {

        log.info("bibleVerse : {}", bibleVerse);

        // 1. 파일저장
        List<Kelly> attachments = new ArrayList<>();
        if(files != null && !files.isEmpty()) {
            for(MultipartFile file : files) {
                if(!file.isEmpty()) {
                    String originalFilename = file.getOriginalFilename();
                    String renamedFilename = Utils.getRenameFilename(originalFilename); // 20230807_142828888_123.jpg
                    File destFile = new File(uploadDirectory, renamedFilename); // 부모디렉토리 생략가능. spring.servlet.multipart.location 값을 사용(기본값)
                    file.transferTo(destFile);

                    Kelly attach = new Kelly();

                    Optional<Kelly> kellyOptional = adminService.findFirstByOrderByKellyIdDesc();

                    if (kellyOptional.isPresent()) {
                        Kelly kelly = kellyOptional.get();
                        attach.setKellyId(kelly.getKellyId() + 1);
                    } else {
                        attach.setKellyId(1);
                    }

                    attach.setKellyOriginalFilename(originalFilename);
                    attach.setKellyRenamedFilename(renamedFilename);
                    attach.setBibleVerse(bibleVerse);
                    attachments.add(attach);
                    adminService.insertKelly(attach);
                }
            }
        }

        redirectAttr.addFlashAttribute("msg", "켈리가 정상적으로 등록되었습니다.");

        return "redirect:/kellyManage";
    }

    @PostMapping("/userUpdateByAdmin.do")
    public String userUpdateByAdmin (@ModelAttribute("userEntity") UserEntity userEntity) {

        Optional<Authority> authorityOptional = adminService.findAuthorityByUserId(userEntity.getUserId());

        if(authorityOptional.isPresent()) {

            Authority authority = authorityOptional.get();

            switch (userEntity.getRankName()) {
                case "간사":
                case "회장":
                case "총무":
                case "서기":
                case "회계":
                    authority.setAuthority("leader");
                    adminService.saveAuthority(authority);
                    adminService.saveUser(userEntity);
                    break;
                case "관리자":
                    authority.setAuthority("admin");
                    adminService.saveAuthority(authority);
                    adminService.saveUser(userEntity);
                    break;
                default:
                    authority.setAuthority("user");
                    adminService.saveAuthority(authority);
                    userEntity.setRankName(null);
                    adminService.saveUser(userEntity);
                    break;
            }

        }

        return "redirect:/admin";
    }

}
