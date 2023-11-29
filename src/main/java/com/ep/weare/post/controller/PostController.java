package com.ep.weare.post.controller;

import com.ep.weare.common.Utils;
import com.ep.weare.post.entity.Announcement;
import com.ep.weare.post.entity.AnnouncementAttach;
import com.ep.weare.post.entity.Worship;
import com.ep.weare.post.service.PostService;
import com.ep.weare.user.controller.UserController;
import com.ep.weare.user.entity.UserEntity;
import com.ep.weare.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    // 공지사항 파일 저장 디렉토리
    @Value("${file.upload.directory.announceAttach:C:/weareAttach/announceAttach}")
    String uploadDirectory;

    // 공지사항 홈페이지
    @GetMapping("/announcement")
    public String getAnnouncement (Model model, HttpSession session,
                                   @PageableDefault(page = 0, size = 10, sort = "announceId", direction = Sort.Direction.DESC) Pageable pageable,
                                   @RequestParam(name = "search", required = false) String search) {

        userController.updateModelWithUserInfo(model, session);

        List<Announcement> announcement = postService.findAllAnnouncement();

        char important = 'o';

        List<Announcement> importantAnnouncement = postService.findByImportantOrderByAnnounceIdDesc(important);

        log.info("important : {}", importantAnnouncement);

        model.addAttribute("noticeList", announcement);
        model.addAttribute("importantNotice", importantAnnouncement);

        // 검색어, 페이징 처리
        Page<Announcement> list;

        if (search != null && !search.isEmpty()) {
            // 검색어가 있는 경우 검색 결과 및 페이징 처리 가져오기
            list = postService.findByKeywordWithPaging(search, pageable);
        } else {
            // 검색어가 없는 경우 전체 공지사항 및 페이징 처리 가져오기
            list = postService.findAll(pageable);
        }


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
            List<AnnouncementAttach> attaches = postService.findById(announcementDetail.getAnnounceId());
            log.info("attaches : {}", attaches);
            model.addAttribute("attaches", attaches);
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
                                @RequestParam(name = "importantCheck", defaultValue = "x") String importantCheck,
                                @RequestParam(name = "announceFile") List<MultipartFile> files) throws IOException {

        // 로그인 계정 정보 추출
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<Announcement> announcementOptional = postService.findFirstByOrderByAnnounceIdDesc();

        if (announcementOptional.isPresent()) {
            Announcement announcementById = announcementOptional.get();
            announcement.setAnnounceId(announcementById.getAnnounceId() + 1);
        } else {
            announcement.setAnnounceId(1);
        }

        announcement.setUserId(username);

        announcement.setImportant(importantCheck.charAt(0));

        log.info("announcement : {}", announcement);

        // 첨부파일
        List<AnnouncementAttach> attachments = new ArrayList<>();
        if(files != null && !files.isEmpty()) {
            for (int i = 0; i<files.size(); i++) {
                MultipartFile file = files.get(i);
                if(!file.isEmpty()) {
                    String originalFilename = file.getOriginalFilename();
                    String renamedFilename = Utils.getRenameFilename(originalFilename); // 20230807_142828888_123.jpg
                    File destFile = new File(uploadDirectory, renamedFilename); // 부모디렉토리 생략가능. spring.servlet.multipart.location 값을 사용(기본값)
                    file.transferTo(destFile);

                    AnnouncementAttach attach = new AnnouncementAttach();

                    Optional<AnnouncementAttach> attachOptional = postService.findFirstByOrderByAnnounceAttachmentIdDesc();

                    if (attachOptional.isPresent()) {
                        AnnouncementAttach announcementAttach = attachOptional.get();
                        attach.setAnnounceAttachmentId(announcementAttach.getAnnounceAttachmentId() + i+1);
                    } else {
                        attach.setAnnounceAttachmentId(i+1);
                    }

                    attach.setAnnounceId(announcement.getAnnounceId());
                    attach.setAnnounceOriginalFilename(originalFilename);
                    attach.setAnnounceRenamedFilename(renamedFilename);

                    attachments.add(attach);

                }
            }
        }

        announcement.setAttaches(attachments);

        Announcement response = postService.saveAnnounce(announcement);

        return "redirect:/announcement";
    }

    // 수정 클릭 시
    @GetMapping("/announceUpdate")
    public String getAnnounceUpdatePage(Model model, HttpSession session,
                                        @RequestParam("id") int id) {
        userController.updateModelWithUserInfo(model, session);
        model.addAttribute("Announcement", new Announcement());

        Optional<Announcement> announcementOptional = postService.findAnnounceById(id);

        log.info("id : {}", id);

        if (announcementOptional.isPresent()) {
            Announcement announcementDetail = announcementOptional.get();
            model.addAttribute("announcementDetail", announcementDetail);
            log.info("announcementDetail : {}", announcementDetail);
        }

        return "post/announceUpdate";
    }

    // 수정 완료 클릭 시
    @PostMapping("announceUpdateComplete.do")
    public String announceUpdateComplete (Model model, HttpSession session,
                                          @RequestParam("announceId") int announceId,
                                          @RequestParam("title") String title,
                                          @RequestParam("content") String content,
                                          @RequestParam(name = "importantCheck", defaultValue = "x") String importantCheck,
                                          @RequestParam(name = "announceFile") List<MultipartFile> files) throws IOException {
        userController.updateModelWithUserInfo(model, session);

        Optional<Announcement> announcementOptional = postService.findAnnounceById(announceId);

        if (announcementOptional.isPresent()) {
            Announcement announcement = announcementOptional.get();

            announcement.setTitle(title);
            announcement.setContent(content);
            announcement.setImportant(importantCheck.charAt(0));

            postService.saveAnnounce(announcement);
        }

        return "redirect:/announceDetail.do?id=" + announceId;
    }

    // 예배 게시판 접속
    @GetMapping("/worship")
    public String getWorshipPage (Model model, HttpSession session,
                                  @PageableDefault(page = 0, size = 10, sort = "worshipDate", direction = Sort.Direction.DESC) Pageable pageable,
                                  @RequestParam(name = "search", required = false) String search) {

        model.addAttribute("worship", new Worship());

        userController.updateModelWithUserInfo(model, session);

        List<Worship> worships = postService.findAllWorship();

        model.addAttribute("worshipList", worships);

        // 검색어, 페이징 처리
        Page<Worship> pageList;

        if (search != null && !search.isEmpty()) {
            // 검색어가 있는 경우 검색 결과 및 페이징 처리 가져오기
            pageList = postService.findWorshipByKeywordWithPaging(search, pageable);
        } else {
            // 검색어가 없는 경우 전체 공지사항 및 페이징 처리 가져오기
            pageList = postService.findWorshipAll(pageable);
        }


        int nowPage = pageList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, pageList.getTotalPages());

        model.addAttribute("list", pageList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "post/worship";
    }

    // 예배 등록
    @PostMapping("/worshipCreate.do")
    public String getWorshipCreate (Model model, HttpSession session,
                                    @ModelAttribute("Worship") Worship worship) {
        // 로그인 계정 정보 추출
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<Worship> worshipOptional = postService.findFirstByOrderByWorshipIdDesc();

        if (worshipOptional.isPresent()) {
            Worship worshipById = worshipOptional.get();
            worship.setWorshipId(worshipById.getWorshipId() + 1);
        } else {
            worship.setWorshipId(1);
        }

        worship.setUserId(username);

        postService.save(worship);


        return "redirect:/worship";
    }

    // 예배 상세
    @GetMapping("/worshipDetail")
    public String getWorshipDetail (Model model, HttpSession session, @RequestParam("id") int id) {
        userController.updateModelWithUserInfo(model, session);

        Optional<Worship> worshipOptional = postService.findWorshipById(id);

        if (worshipOptional.isPresent()) {
            Worship worshipDetail = worshipOptional.get();
            model.addAttribute("worshipDetail", worshipDetail);
            log.info("worshipDetail : {}", worshipDetail);

            String videoId = null;
            String pattern = "(?<=\\/live\\/|\\/v\\/|\\/e\\/|\\/embed\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed\\u200C\\u200B2F|youtu.be%2F|\\/e%2F|watch\\?v=|\\?v=)([^#\\&\\?\\n]*[^\\&\\?\\n])";
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(worshipDetail.getLink());

            if (matcher.find()) {
                videoId = matcher.group();
            }

            log.info("videoId : {}", videoId);
            model.addAttribute("videoId", videoId);
        }

        return "post/worshipDetail";
    }

    @GetMapping("/question")
    public String getQuestionPage(Model model, HttpSession session) {



        return "post/question";
    }

}
