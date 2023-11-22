package com.ep.weare.post.controller;

import com.ep.weare.post.dto.AnnounceWithUser;
import com.ep.weare.post.dto.AnnounceWithUserInterface;
import com.ep.weare.post.entity.Announcement;
import com.ep.weare.post.service.PostService;
import com.ep.weare.user.controller.UserController;
import com.ep.weare.user.entity.UserEntity;
import com.ep.weare.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class PostController {

    private PostService postService;
    private UserService userService;

    private UserController userController;

    @Autowired
    public PostController(PostService postService, UserService userService, UserController userController) {

        this.postService = postService;
        this.userService = userService;
        this.userController = userController;
    }

    // 공지사항 홈페이지
    @GetMapping("/announcement")
    public String getAnnouncement (Model model, HttpSession session,
                                   @PageableDefault(page = 0, size = 10, sort = "announceId", direction = Sort.Direction.DESC) Pageable pageable) {

        userController.updateModelWithUserInfo(model, session);

        List<Announcement> announcement = postService.findAllAnnouncement();

        char important = 'o';

        List<Announcement> importantAnnouncement = postService.findByImportant(important);

        log.info("important : {}", importantAnnouncement);

        model.addAttribute("noticeList", announcement);
        model.addAttribute("importantNotice", importantAnnouncement);

        // 페이징 처리
        Page<Announcement> list = postService.findAll(pageable);

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "post/announcement";
    }

    // 공지사항 상세보기
    @GetMapping("/announceDetail.do")
    public String getAnnounceDetail (Model model, HttpSession session, @RequestParam("id") int id) {
        userController.updateModelWithUserInfo(model, session);

        Optional<Announcement> announcementOptional = postService.findAnnounceById(id);

        if (announcementOptional.isPresent()) {
            Announcement announcementDetail = announcementOptional.get();
            model.addAttribute("announcementDetail", announcementDetail);
            log.info("announcementDetail : {}", announcementDetail);
        }

        return "post/announceDetail";
    }

    // 글쓰기 페이지
    @GetMapping("/announceCreate.do")
    public String getAnnounceCreate (Model model, HttpSession session) {
        userController.updateModelWithUserInfo(model, session);
        model.addAttribute("Announcement", new Announcement());
        return "post/announceCreate";
    }

    // 글쓰기 완료 버튼 클릭시
    @PostMapping("/announceCreateComplete.do")
    public String saveAnnounce (Model model, HttpSession session,
                                @ModelAttribute("Announcement") Announcement announcement,
                                @RequestParam(name = "importantCheck", defaultValue = "x") String importantCheck) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<UserEntity> userEntityOptional = userService.findById(username);

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();

            announcement.setUserId(userEntity.getUserId());

            announcement.setImportant(importantCheck.charAt(0));

            log.info("announcement : {}", announcement);

            Announcement response = postService.saveAnnounce(announcement);
        }

        return "redirect:/announcement";
    }
}
