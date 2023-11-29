package com.ep.weare.user.controller;

import com.ep.weare.admin.entity.Kelly;
import com.ep.weare.admin.service.AdminService;
import com.ep.weare.post.entity.Announcement;
import com.ep.weare.post.service.AnnounceService;
import com.ep.weare.user.entity.Gender;
import com.ep.weare.user.entity.UserCheck;
import com.ep.weare.user.entity.UserEntity;
import com.ep.weare.user.service.UserService;
import com.google.common.collect.Lists;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
// 데이터만 주고 받는 RestController
public class UserController {

    // userService 자동 Bean 등록
    private UserService userService;

    private PasswordEncoder passwordEncoder;

    private AnnounceService postService;

    private AdminService adminService;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder, AnnounceService postService,
                          AdminService adminService) {

        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.postService = postService;
        this.adminService = adminService;
    }

    // 메인 화면 뷰단 연결
    // ModelAndView를 쓰는 이유는 RESTFUL 사용을 위한 @RestController로 인해서임. (@Controller 사용시, String으로 연결 가능)
    @GetMapping("/home")
    public String getHome(Model model, HttpSession session) {
        updateModelWithUserInfo(model, session);

        List<Announcement> announcement = postService.findTop3ByOrderByAnnounceIdDesc();

        model.addAttribute("top3Announcement", announcement);

        List<Kelly> getTop3Kellys = adminService.findTop3ByOrderByKellyIdDesc();
        Kelly kellyTop1 = null;
        Kelly kellyTop2 = null;
        Kelly kellyTop3 = null;

        if (!getTop3Kellys.isEmpty()) {
            kellyTop1 = getTop3Kellys.get(0);
            if (getTop3Kellys.size() > 1) {
                kellyTop2 = getTop3Kellys.get(1);
                if (getTop3Kellys.size() > 2) {
                    kellyTop3 = getTop3Kellys.get(2);
                }
            }
        }

        model.addAttribute("kelly1", kellyTop1);
        model.addAttribute("kelly2", kellyTop2);
        model.addAttribute("kelly3", kellyTop3);

        return "home";
    }

    // 2023 우리는 전체결산 뷰단 연결
    @GetMapping("/2023weare")
    public String getWeare2023(Model model) {
        return "user/2023weare";
    }

    // 회원가입 뷰단 연결
    @GetMapping("/signup")
    public String createUser(Model model) {
        model.addAttribute("userEntity", new UserEntity());
        return "user/signup";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String getLogin(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "exception", required = false) String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        log.info(error);
        log.info(exception);

        return "user/login";
    }

    // 로그인 실패
    @GetMapping("/loginErr")
    public String getLoginFail(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "exception", required = false) String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        log.info(exception);

        return "user/login";
//        return "home";
    }

    // 로그아웃
    @PostMapping("/logout.do")
    public String getLogout(Model model) {
        model.addAttribute("userEntity", new UserEntity());
        return "home";
    };

//    // 회원가입 완료 뷰단 연결
//    @GetMapping("/signupComplete")
//    public String getSignUpComplete(Model model) {
//        return "user/signupComplete";
//    }

    // 회원가입 완료
    @PostMapping("/userCreate.do")
    public String createUser(@ModelAttribute("userEntity") UserEntity userEntity, BindingResult bindingResult,
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

        // DB 저장
        UserEntity response = userService.createUser(userEntity);

        // 완료되면 떠야하는데 안뜸..
        redirectAttr.addFlashAttribute("msg", "회원가입을 축하드립니다.");

        return "user/signupComplete";
//        ModelAndView mv = new ModelAndView("redirect:/user/signupComplete");
//        return mv;

        // 그래서 회원가입 완료 페이지로 연결되는 로직
//        RedirectView rv = new RedirectView("/weare/signupComplete");
//        rv.setExposeModelAttributes(false);
//        return new ModelAndView(rv);

    }

    // 마이페이지 접속
    @GetMapping("/mypage")
    public String getMyPage(Model model, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<UserEntity> userEntityOptional = userService.findById(username);

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();

            model.addAttribute("myAccount", userEntity);
        }

        return "user/mypage";
    }

    public void updateModelWithUserInfo(Model model, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<UserEntity> userEntityOptional = userService.findById(username);

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();


            model.addAttribute("userCheck", userEntity.getUserCheck());

            session.setAttribute("loggedInUser", userEntity);

        }

    }

    @GetMapping("/kelly")
    public String getKelly(Model model) {

        List<Kelly> getAllKellys = adminService.findAllByOrderByKellyIdDesc();

        List<List<Kelly>> chunkedKellys = Lists.partition(getAllKellys, 2);

        model.addAttribute("kellys", chunkedKellys);

        return "user/kelly";
    }

}
