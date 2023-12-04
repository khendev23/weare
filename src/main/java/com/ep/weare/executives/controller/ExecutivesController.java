package com.ep.weare.executives.controller;

import com.ep.weare.common.Utils;
import com.ep.weare.executives.entity.Executives;
import com.ep.weare.executives.entity.ExecutivesAttachment;
import com.ep.weare.executives.service.ExecutivesService;
import com.ep.weare.user.controller.UserController;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class ExecutivesController {

    private final ExecutivesService executivesService;
    private final UserController userController;

    public ExecutivesController(ExecutivesService executivesService, UserController userController) {
        this.executivesService = executivesService;
        this.userController = userController;
    }

    // 임원 게시판 파일 저장 디렉토리
    @Value("${file.upload.directory.executivesAttach:C:/weareAttach/executivesAttach}")
    String uploadDirectory;

    @GetMapping("/executives")
    public String getExecutivesPage(Model model, HttpSession session,
                                  @PageableDefault(page = 0, size = 10, sort = "executivesId", direction = Sort.Direction.DESC) Pageable pageable,
                                  @RequestParam(name = "category", required = false) List<String> categories,
                                  @RequestParam(name = "search", required = false) String search) {

        userController.updateModelWithUserInfo(model, session);

        // 검색어, 페이징 처리
        Page<Executives> pageList;

//        if (search != null && !search.isEmpty()) {
//            // 검색어가 있는 경우 검색 결과 및 페이징 처리 가져오기
//            pageList = ministryService.findMinistryByKeywordWithPaging(search, pageable);
//        } else {
//            // 검색어가 없는 경우 전체 공지사항 및 페이징 처리 가져오기
//            pageList = ministryService.findMinistryAll(pageable);
//        }

        if ((categories != null && !categories.isEmpty()) && (search != null && !search.isEmpty())) {
            // categories search가 모두 있는 경우 검색 결과 및 페이징 처리 가져오기
            pageList = executivesService.findExectuviesByCategoriesAndKeywordWithPaging(categories, search, pageable);
        } else if (categories != null && !categories.isEmpty()) {
            // msteamNames만 있는 경우 검색 결과 및 페이징 처리 가져오기
            pageList = executivesService.findExectuviesByCategoriesAndKeywordWithPaging(categories, null, pageable);
        } else if (search != null && !search.isEmpty()) {
            // search만 있는 경우 검색 결과 및 페이징 처리 가져오기
            pageList = executivesService.findExectuviesByCategoriesAndKeywordWithPaging(Collections.emptyList(), search, pageable);
        } else {
            // msteamNames와 search가 모두 없는 경우 전체 공지사항 및 페이징 처리 가져오기
            pageList = executivesService.findExecutivesAll(pageable);
        }

        int nowPage = pageList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, pageList.getTotalPages());

        model.addAttribute("list", pageList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "post/executives";

    }

    // 글쓰기 페이지
    @GetMapping("/executivesCreate")
    public String getExecutivesCreate (Model model, HttpSession session) {
        userController.updateModelWithUserInfo(model, session);

        model.addAttribute("Executives", new Executives());
        return "post/executivesCreate";
    }

    // 임원 게시판 글쓰기 완료
    @PostMapping("/executivesCreateComplete.do")
    public String saveMinistry (Model model, HttpSession session,
                                @ModelAttribute("Executives") Executives executives,
                                @RequestParam(name = "executivesFile") List<MultipartFile> files) throws IOException {

        // 로그인 계정 정보 추출
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // ministryId 설정
        Optional<Executives> executivesOptional = executivesService.findFirstByOrderByExecutivesIdDesc();

        if (executivesOptional.isPresent()) {
            int executivesId = executivesOptional.get().getExecutivesId();
            executives.setExecutivesId(executivesId + 1);
        } else {
            executives.setExecutivesId(1);
        }

        executives.setUserId(username);

        log.info("executives : {}", executives);

        // 첨부파일
        List<ExecutivesAttachment> attachments = new ArrayList<>();
        if(files != null && !files.isEmpty()) {
            for (int i = 0; i<files.size(); i++) {
                MultipartFile file = files.get(i);
                if(!file.isEmpty()) {
                    String originalFilename = file.getOriginalFilename();
                    String renamedFilename = Utils.getRenameFilename(originalFilename); // 20230807_142828888_123.jpg
                    File destFile = new File(uploadDirectory, renamedFilename); // 부모디렉토리 생략가능. spring.servlet.multipart.location 값을 사용(기본값)
                    file.transferTo(destFile);

                    ExecutivesAttachment attach = new ExecutivesAttachment();

                    Optional<ExecutivesAttachment> attachOptional = executivesService.findFirstByOrderByExecutivesAttachmentIdDesc();

                    if (attachOptional.isPresent()) {
                        int executivesAttachmentId = attachOptional.get().getExecutivesAttachmentId();
                        attach.setExecutivesAttachmentId(executivesAttachmentId + i+1);
                    } else {
                        attach.setExecutivesAttachmentId(i+1);
                    }

                    attach.setExecutivesId(executives.getExecutivesId());
                    attach.setExecutivesOriginalFilename(originalFilename);
                    attach.setExecutivesRenamedFilename(renamedFilename);

                    attachments.add(attach);

                }
            }
        }

        executives.setAttachments(attachments);

        executivesService.saveExecutives(executives);

        return "redirect:/executives";
    }

    // 사역 게시판 상세보기
    @GetMapping("/executivesDetail")
    public String getExecutivesDetail (Model model, HttpSession session, @RequestParam("id") int id) {
        userController.updateModelWithUserInfo(model, session);

        Optional<Executives> executivesOptional = executivesService.findExecutivesById(id);

        if (executivesOptional.isPresent()) {
            Executives executivesDetail = executivesOptional.get();
            model.addAttribute("executivesDetail", executivesDetail);
            log.info("executivesDetail : {}", executivesDetail);
            List<ExecutivesAttachment> attaches = executivesService.findById(executivesDetail.getExecutivesId());
            log.info("attaches : {}", attaches);
            model.addAttribute("attaches", attaches);
        }

//        // 댓글만(댓글레밸 == 1인 애들만)
//        List<CommentMinistry> comments = commentService.findCommentMinistryById(id);
//
//        // 대댓글만(댓글레밸 == 2인 애들만)
//        List<CommentMinistry> replies = commentService.findReplyMinistryById(id);
//
//        model.addAttribute("comments", comments);
//        model.addAttribute("replies", replies);
//
//        model.addAttribute("CommentMinistry", new CommentMinistry());
//
//        log.info("replies : {}", replies);

        return "post/executivesDetail";
    }

    // 임원 게시글 수정 클릭 시
    @GetMapping("/executivesUpdate")
    public String getExecutivesUpdatePage(Model model, HttpSession session,
                                          @RequestParam("id") int id) {
        // 로그인 계정 정보 추출
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        model.addAttribute("Executives", new Executives());

        Optional<Executives> executivesOptional = executivesService.findExecutivesById(id);

        if (executivesOptional.isPresent()) {
            Executives executivesDetail = executivesOptional.get();

            String postAuthor = executivesDetail.getUserId();

            if(!username.equals(postAuthor)) {
                return "redirect:/executives";
            }

            model.addAttribute("executivesDetail", executivesDetail);
            List<ExecutivesAttachment> attaches = executivesService.findById(executivesDetail.getExecutivesId());
            model.addAttribute("attaches", attaches);
        }

        return "post/executivesUpdate";
    }

    // 수정 완료 클릭 시
    @PostMapping("executivesUpdateComplete.do")
    public String executivesUpdateComplete (Model model, HttpSession session,
                                          @RequestParam("executivesId") int executivesId,
                                          @RequestParam("category") String category,
                                          @RequestParam("title") String title,
                                          @RequestParam("content") String content,
                                          @RequestParam(value = "fileCheckBox", required = false) List<Integer> attachmentIds,
                                          @RequestParam(name = "executivesFile") List<MultipartFile> files) throws IOException {
        userController.updateModelWithUserInfo(model, session);

        log.info("attachmentIds : {}", attachmentIds);

        if(attachmentIds != null){
            for (Integer attachmentId : attachmentIds) {
                log.info("attachmentId : {}", attachmentId);
                executivesService.deleteExecutivesAttachmentById(attachmentId);
            }
        }

        // 첨부파일
        List<ExecutivesAttachment> attachments = new ArrayList<>();
        if(files != null && !files.isEmpty()) {
            for (int i = 0; i<files.size(); i++) {
                MultipartFile file = files.get(i);
                if(!file.isEmpty()) {
                    String originalFilename = file.getOriginalFilename();
                    String renamedFilename = Utils.getRenameFilename(originalFilename); // 20230807_142828888_123.jpg
                    File destFile = new File(uploadDirectory, renamedFilename); // 부모디렉토리 생략가능. spring.servlet.multipart.location 값을 사용(기본값)
                    file.transferTo(destFile);

                    ExecutivesAttachment attach = new ExecutivesAttachment();

                    Optional<ExecutivesAttachment> attachOptional = executivesService.findFirstByOrderByExecutivesAttachmentIdDesc();

                    if (attachOptional.isPresent()) {
                        ExecutivesAttachment executivesAttachment = attachOptional.get();
                        attach.setExecutivesAttachmentId(executivesAttachment.getExecutivesAttachmentId() + i+1);
                    } else {
                        attach.setExecutivesAttachmentId(i+1);
                    }

                    attach.setExecutivesId(executivesId);
                    attach.setExecutivesOriginalFilename(originalFilename);
                    attach.setExecutivesRenamedFilename(renamedFilename);

                    attachments.add(attach);
                }
            }
        }

        Optional<Executives> executivesOptional = executivesService.findExecutivesById(executivesId);

        if (executivesOptional.isPresent()) {
            Executives executives = executivesOptional.get();

            executives.setTitle(title);
            executives.setContent(content);

            executives.setAttachments(attachments);

            executives.setCategory(category);

            executivesService.saveExecutives(executives);
        }

        return "redirect:/executivesDetail?id=" + executivesId;
    }

}
