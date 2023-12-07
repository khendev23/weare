package com.ep.weare.ministry.controller;

import com.ep.weare.comment.dto.CommentCountByMinistry;
import com.ep.weare.comment.entity.CommentMinistry;
import com.ep.weare.comment.entity.CommentQuestion;
import com.ep.weare.comment.service.CommentService;
import com.ep.weare.common.Utils;
import com.ep.weare.ministry.entity.Ministry;
import com.ep.weare.ministry.entity.MinistryAttachment;
import com.ep.weare.ministry.service.MinistryService;
import com.ep.weare.post.entity.Announcement;
import com.ep.weare.post.entity.AnnouncementAttach;
import com.ep.weare.post.entity.Question;
import com.ep.weare.post.service.PostService;
import com.ep.weare.user.controller.UserController;
import com.ep.weare.user.entity.MinistryTeam;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class MinistryController {

    private final MinistryService ministryService;
    private final UserController userController;
    private final CommentService commentService;
    private final PostService postService;

    public MinistryController(MinistryService ministryService, UserController userController,
                              CommentService commentService, PostService postService) {
        this.ministryService = ministryService;
        this.userController = userController;
        this.commentService = commentService;
        this.postService = postService;
    }

    // 사역게시판 파일 저장 디렉토리
    @Value("${file.upload.directory.ministryAttach:C:/weareAttach/ministryAttach}")
    String uploadDirectory;

    @GetMapping("/ministry")
    public String getMinistryPage(Model model, HttpSession session,
                                  @PageableDefault(page = 0, size = 10, sort = "ministryId", direction = Sort.Direction.DESC) Pageable pageable,
                                  @RequestParam(name = "msteamNames", required = false) List<String> msteamNames,
                                  @RequestParam(name = "search", required = false) String search) {

        userController.updateModelWithUserInfo(model, session);

        // 사역팀 목록 전달
        List<MinistryTeam> ministryTeams = ministryService.findAll();

        model.addAttribute("ministryTeams", ministryTeams);

        // 검색어, 페이징 처리
        Page<Ministry> pageList;

//        if (search != null && !search.isEmpty()) {
//            // 검색어가 있는 경우 검색 결과 및 페이징 처리 가져오기
//            pageList = ministryService.findMinistryByKeywordWithPaging(search, pageable);
//        } else {
//            // 검색어가 없는 경우 전체 공지사항 및 페이징 처리 가져오기
//            pageList = ministryService.findMinistryAll(pageable);
//        }

        log.info("체크한 사역팀 : {}", msteamNames);

        if ((msteamNames != null && !msteamNames.isEmpty()) && (search != null && !search.isEmpty())) {
            // msteamNames와 search가 모두 있는 경우 검색 결과 및 페이징 처리 가져오기
            pageList = ministryService.findMinistriesByMsteamNamesAndKeywordWithPaging(msteamNames, search, pageable);
            log.info("1번 동작함");
        } else if (msteamNames != null && !msteamNames.isEmpty()) {
            // msteamNames만 있는 경우 검색 결과 및 페이징 처리 가져오기
            pageList = ministryService.findMinistriesByMsteamNamesAndKeywordWithPaging(msteamNames, null, pageable);
            log.info("2번 동작함");
        } else if (search != null && !search.isEmpty()) {
            // search만 있는 경우 검색 결과 및 페이징 처리 가져오기
            pageList = ministryService.findMinistriesByMsteamNamesAndKeywordWithPaging(Collections.emptyList(), search, pageable);
            log.info("3번 동작함");
        } else {
            // msteamNames와 search가 모두 없는 경우 전체 공지사항 및 페이징 처리 가져오기
            pageList = ministryService.findMinistryAll(pageable);
            log.info("4번 동작함");
        }

        // 각 질문에 대한 댓글 수 가져오기
        List<CommentCountByMinistry> commentCounts = commentService.getCommentCountsForMinistry();
        model.addAttribute("commentCounts", commentCounts);

        int nowPage = pageList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, pageList.getTotalPages());

        model.addAttribute("list", pageList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "post/ministry";

    }

    // 글쓰기 페이지
    @GetMapping("/ministryCreate")
    public String getAnnounceCreate (Model model, HttpSession session) {
        userController.updateModelWithUserInfo(model, session);

        List<MinistryTeam> ministryTeams = ministryService.findAll();

        model.addAttribute("ministryTeams", ministryTeams);

        model.addAttribute("Ministry", new Ministry());
        return "post/ministryCreate";
    }

    // 사역 게시판 글쓰기 완료
    @PostMapping("/ministryCreateComplete.do")
    public String saveMinistry (Model model, HttpSession session,
                                @ModelAttribute("Ministry") Ministry ministry,
                                @RequestParam(name = "ministryFile") List<MultipartFile> files) throws IOException {

        // 로그인 계정 정보 추출
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // ministryId 설정
        Optional<Ministry> ministryOptional = ministryService.findFirstByOrderByMinistryIdDesc();

        if (ministryOptional.isPresent()) {
            int ministryId = ministryOptional.get().getMinistryId();
            ministry.setMinistryId(ministryId + 1);
        } else {
            ministry.setMinistryId(1);
        }

        ministry.setUserId(username);

        log.info("ministry : {}", ministry);

        // 첨부파일
        List<MinistryAttachment> attachments = new ArrayList<>();
        if(files != null && !files.isEmpty()) {
            for (int i = 0; i<files.size(); i++) {
                MultipartFile file = files.get(i);
                if(!file.isEmpty()) {
                    String originalFilename = file.getOriginalFilename();
                    String renamedFilename = Utils.getRenameFilename(originalFilename); // 20230807_142828888_123.jpg
                    File destFile = new File(uploadDirectory, renamedFilename); // 부모디렉토리 생략가능. spring.servlet.multipart.location 값을 사용(기본값)
                    file.transferTo(destFile);

                    MinistryAttachment attach = new MinistryAttachment();

                    Optional<MinistryAttachment> attachOptional = ministryService.findFirstByOrderByMinistryAttachmentIdDesc();

                    if (attachOptional.isPresent()) {
                        int ministryAttachmentId = attachOptional.get().getMinistryAttachmentId();
                        attach.setMinistryAttachmentId(ministryAttachmentId + i+1);
                    } else {
                        attach.setMinistryAttachmentId(i+1);
                    }

                    attach.setMinistryId(ministry.getMinistryId());
                    attach.setMinistryOriginalFilename(originalFilename);
                    attach.setMinistryRenamedFilename(renamedFilename);

                    attachments.add(attach);

                }
            }
        }

        ministry.setAttachments(attachments);

        ministryService.saveMinistry(ministry);

        return "redirect:/ministry";
    }

    // 사역 게시판 상세보기
    @GetMapping("/ministryDetail")
    public String getAnnounceDetail (Model model, HttpSession session, @RequestParam("id") int id) {
        userController.updateModelWithUserInfo(model, session);

        Optional<Ministry> ministryOptional = ministryService.findMinistryById(id);

        if (ministryOptional.isPresent()) {
            Ministry ministryDetail = ministryOptional.get();
            model.addAttribute("ministryDetail", ministryDetail);
            log.info("ministryDetail : {}", ministryDetail);
            List<MinistryAttachment> attaches = ministryService.findById(ministryDetail.getMinistryId());
            log.info("attaches : {}", attaches);
            model.addAttribute("attaches", attaches);
        }

        // 댓글만(댓글레밸 == 1인 애들만)
        List<CommentMinistry> comments = commentService.findCommentMinistryById(id);

        // 대댓글만(댓글레밸 == 2인 애들만)
        List<CommentMinistry> replies = commentService.findReplyMinistryById(id);

        model.addAttribute("comments", comments);
        model.addAttribute("replies", replies);

        model.addAttribute("CommentMinistry", new CommentMinistry());

        log.info("replies : {}", replies);

        return "post/ministryDetail";
    }

    // 사역 게시글 수정 클릭 시
    @GetMapping("/ministryUpdate")
    public String getMinistryUpdatePage(Model model, HttpSession session,
                                        @RequestParam("id") int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        model.addAttribute("Ministry", new Ministry());

        Optional<Ministry> ministryOptional = ministryService.findMinistryById(id);

        if (ministryOptional.isPresent()) {
            Ministry ministryDetail = ministryOptional.get();

            String postAuthor = ministryDetail.getUserId();

            if(!username.equals(postAuthor)) {
                return "redirect:/executives";
            }

            model.addAttribute("ministryDetail", ministryDetail);
            log.info("ministryDetail : {}", ministryDetail);

            List<MinistryAttachment> attaches = ministryService.findById(ministryDetail.getMinistryId());
            model.addAttribute("attaches", attaches);
        }

        List<MinistryTeam> ministryTeams = ministryService.findAll();

        model.addAttribute("ministryTeams", ministryTeams);

        return "post/ministryUpdate";
    }

    // 수정 완료 클릭 시
    @PostMapping("ministryUpdateComplete.do")
    public String ministryUpdateComplete (Model model, HttpSession session,
                                          @RequestParam("ministryId") int ministryId,
                                          @RequestParam("msteamName") String msteamName,
                                          @RequestParam("title") String title,
                                          @RequestParam("content") String content,
                                          @RequestParam(value = "fileCheckBox", required = false) List<Integer> attachmentIds,
                                          @RequestParam(name = "announceFile") List<MultipartFile> files) throws IOException {
        userController.updateModelWithUserInfo(model, session);

        log.info("attachmentIds : {}", attachmentIds);

        if(attachmentIds != null){
            for (Integer attachmentId : attachmentIds) {
                log.info("attachmentId : {}", attachmentId);
                ministryService.deleteMinistryAttachmentById(attachmentId);
            }
        }

        // 첨부파일
        List<MinistryAttachment> attachments = new ArrayList<>();
        if(files != null && !files.isEmpty()) {
            for (int i = 0; i<files.size(); i++) {
                MultipartFile file = files.get(i);
                if(!file.isEmpty()) {
                    String originalFilename = file.getOriginalFilename();
                    String renamedFilename = Utils.getRenameFilename(originalFilename); // 20230807_142828888_123.jpg
                    File destFile = new File(uploadDirectory, renamedFilename); // 부모디렉토리 생략가능. spring.servlet.multipart.location 값을 사용(기본값)
                    file.transferTo(destFile);

                    MinistryAttachment attach = new MinistryAttachment();

                    Optional<MinistryAttachment> attachOptional = ministryService.findFirstByOrderByMinistryAttachmentIdDesc();

                    if (attachOptional.isPresent()) {
                        MinistryAttachment ministryAttach = attachOptional.get();
                        attach.setMinistryAttachmentId(ministryAttach.getMinistryAttachmentId() + i+1);
                    } else {
                        attach.setMinistryAttachmentId(i+1);
                    }

                    attach.setMinistryId(ministryId);
                    attach.setMinistryOriginalFilename(originalFilename);
                    attach.setMinistryRenamedFilename(renamedFilename);

                    attachments.add(attach);
                }
            }
        }

        Optional<Ministry> ministryOptional = ministryService.findMinistryById(ministryId);

        if (ministryOptional.isPresent()) {
            Ministry ministry = ministryOptional.get();

            ministry.setTitle(title);
            ministry.setContent(content);

            ministry.setAttachments(attachments);

            ministry.setMsteamName(msteamName);

            ministryService.saveMinistry(ministry);
        }

        return "redirect:/ministryDetail?id=" + ministryId;
    }

}
