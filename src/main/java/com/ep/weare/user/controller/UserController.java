package com.ep.weare.user.controller;

import com.ep.weare.user.dto.UserSignupDTO;
import com.ep.weare.user.entity.Gender;
import com.ep.weare.user.entity.UserCheck;
import com.ep.weare.user.entity.UserEntity;
import com.ep.weare.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@RestController
@Slf4j
//@RequestMapping(value = "/weare")
public class UserController {

    // userService 자동 Bean 등록
    private UserService userService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {

        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // 메인 화면 뷰단 연결
    // ModelAndView를 쓰는 이유는 RESTFUL 사용을 위한 @RestController로 인해서임. (@Controller 사용시, String으로 연결 가능)
    @GetMapping("/home")
    public ModelAndView getHome(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<UserEntity> userEntityOptional = userService.findById(username);

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();

            log.info("userCheck : {}", userEntity.getUserCheck());

            model.addAttribute("userCheck", userEntity.getUserCheck());
        }


        model.addAttribute("username", username);
        ModelAndView mv = new ModelAndView("home");
        return mv;
    }

    // 2023 우리는 전체결산 뷰단 연결
    @GetMapping("/2023weare")
    public ModelAndView getWeare2023(Model model) {
        ModelAndView mv = new ModelAndView("user/2023weare");
        return mv;
    }

    // 회원가입 뷰단 연결
    @GetMapping("/signup")
    public ModelAndView createUser(Model model) {
        model.addAttribute("userEntity", new UserEntity());
        ModelAndView mv = new ModelAndView("user/signup");
        return mv;
    }

    // 로그인 페이지
    @GetMapping("/login")
    public ModelAndView getLogin(Model model) {
        model.addAttribute("userEntity", new UserEntity());
        ModelAndView mv = new ModelAndView("user/login");
        return mv;
    }

    // 로그인 실패
    @GetMapping("/loginFail")
    public ModelAndView getLoginFail(Model model) {
        model.addAttribute("userEntity", new UserEntity());
        ModelAndView mv = new ModelAndView("user/loginFail");
        return mv;
    }

    // 로그아웃
    @PostMapping("/logout.do")
    public ModelAndView getLogout(Model model) {
        model.addAttribute("userEntity", new UserEntity());
        ModelAndView mv = new ModelAndView("/home");
        return mv;
    };

    // 회원가입 완료 뷰단 연결
    @GetMapping("/signupComplete")
    public ModelAndView getSignUpComplete(Model model) {
        ModelAndView mv = new ModelAndView("user/signupComplete");
        return mv;
    }

    // 회원가입 완료
    @PostMapping("/signup/userCreate.do")
    public ModelAndView createUser(@ModelAttribute("userEntity") UserEntity userEntity, BindingResult bindingResult,
                               @RequestParam("gender") String gender, @RequestParam("roadAddress") String roadAddress,
                               @RequestParam("detailAddress") String detailAddress, RedirectAttributes redirectAttr) {

//        if (bindingResult.hasErrors()) {
//            ObjectError error = bindingResult.getAllErrors().get(0);
//            redirectAttr.addFlashAttribute("msg", error.getDefaultMessage());
//
//            ModelAndView mv = new ModelAndView("user/signup");
//            return mv;
//        }

        String rawPassword = userEntity.getUserPw();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        userEntity.setUserPw(encodedPassword);

        // 성별에 따른 enum값 배치
        if (gender.equals("남")) {
            userEntity.setGender(Gender.M);
        } else {
            userEntity.setGender(Gender.F);
        }

        // 풀 주소로 DB 저장 위함
        String fullAddress = roadAddress + " " + detailAddress;
        userEntity.setAddress(fullAddress);

        userEntity.setUserCheck(UserCheck.x);

        log.info(userEntity.toString());

        // DB 저장
        UserEntity response = userService.createUser(userEntity);

        // 완료되면 떠야하는데 안뜸..
        redirectAttr.addFlashAttribute("msg", "회원가입을 축하드립니다.");

//        return "redirect:/home";
//        ModelAndView mv = new ModelAndView("redirect:/user/signupComplete");
//        return mv;

        // 그래서 회원가입 완료 페이지로 연결되는 로직
        RedirectView rv = new RedirectView("/weare/signupComplete");
        rv.setExposeModelAttributes(false);
        return new ModelAndView(rv);

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