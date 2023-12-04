package com.ep.weare.post.controller;

import com.ep.weare.comment.service.CommentService;
import com.ep.weare.common.Utils;
import com.ep.weare.post.service.PostService;
import com.ep.weare.user.controller.UserController;
import com.ep.weare.user.service.UserService;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@Slf4j
public class PostRestController {

    private final PostService postService;
    private final UserService userService;
    private final UserController userController;
    private final CommentService commentService;

    @Autowired
    public PostRestController(PostService postService, UserService userService, UserController userController,
                              CommentService commentService) {

        this.postService = postService;
        this.userService = userService;
        this.userController = userController;
        this.commentService = commentService;

    }

    // 공지사항 삭제
    @PostMapping("/announceDelete.do")
    public ResponseEntity<String> deleteAnnounce (Model model, HttpSession session,
                                          @RequestParam int announceId) {
        userController.updateModelWithUserInfo(model, session);

        postService.deleteAnnounceById(announceId);

        return ResponseEntity.ok("공지사항이 삭제되었습니다.");
    }

    // 공지사항 이미지 업로드(제출은 안한 상태)
    @PostMapping(value="/uploadAnnounceimage", produces = "application/json")
    public JsonObject uploadAnnounceImageFile(@RequestParam("file") MultipartFile multipartFile) {

        JsonObject jsonObject = new JsonObject();

        String fileRoot = "C:\\weareAttach\\announceImage\\";	//저장될 외부 파일 경로
        String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자

        String savedFileName = UUID.randomUUID() + extension;	//저장될 파일 명

        File targetFile = new File(fileRoot + savedFileName);

        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장
            jsonObject.addProperty("url", "/weare/announceImage/"+savedFileName);
            jsonObject.addProperty("responseCode", "success");

        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }

        return jsonObject;
    }

    // 질문 이미지 업로드(제출은 안한 상태)
    @PostMapping(value="/uploadQuestionimage", produces = "application/json")
    public JsonObject uploadQuestionImageFile(@RequestParam("file") MultipartFile multipartFile) {

        JsonObject jsonObject = new JsonObject();

        String fileRoot = "C:\\weareAttach\\questionImage\\";	//저장될 외부 파일 경로
        String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명

        String savedFileName = Utils.getRenameFilename(originalFileName);	//저장될 파일 명

        File targetFile = new File(fileRoot + savedFileName);

        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장
            jsonObject.addProperty("url", "/weare/questionImage/"+savedFileName);
            jsonObject.addProperty("responseCode", "success");

        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }

        return jsonObject;
    }

    // 질문 삭제
    @PostMapping("/questionDelete.do")
    public ResponseEntity<String> deleteQuestion (Model model, HttpSession session,
                                                  @RequestParam int questionId) {
        userController.updateModelWithUserInfo(model, session);

        commentService.deleteByQuestionId(questionId);
        postService.deleteQuestionById(questionId);

        return ResponseEntity.ok("성공적으로 삭제되었습니다.");
    }

}
