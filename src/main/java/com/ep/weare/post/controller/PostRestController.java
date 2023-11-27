package com.ep.weare.post.controller;

import com.ep.weare.post.service.PostService;
import com.ep.weare.user.controller.UserController;
import com.ep.weare.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PostRestController {

    private PostService postService;
    private UserService userService;

    private UserController userController;

    @Autowired
    public PostRestController(PostService postService, UserService userService, UserController userController) {

        this.postService = postService;
        this.userService = userService;
        this.userController = userController;
    }

    // 공지사항 삭제
    @PostMapping("/announceDelete.do")
    public ResponseEntity<String> deleteAnnounce (Model model, HttpSession session,
                                          @RequestParam int announceId) {
        userController.updateModelWithUserInfo(model, session);

        postService.deleteAnnounceById(announceId);

        return ResponseEntity.ok("공지사항이 삭제되었습니다.");
    }

}
