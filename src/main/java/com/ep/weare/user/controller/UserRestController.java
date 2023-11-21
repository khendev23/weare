package com.ep.weare.user.controller;

import com.ep.weare.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
// view단 연결해주는 Controller
public class UserRestController {

    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {

        this.userService = userService;

    }

    // 회원가입 시 ID 중복체크
    @GetMapping("/signup/checkDuplicatedId.do")
    public ResponseEntity<?> checkDuplicatedId(@RequestParam("userId") String userId) {

        log.info("userId = " + userId);

        System.out.println(userId);

        boolean isDuplicate = userService.isUserIdDuplicate(userId);

        // 직접 boolean 값을 반환
        return ResponseEntity.ok(isDuplicate);

    }

}
