package com.ep.weare.ministry.controller;

import com.ep.weare.comment.service.CommentService;
import com.ep.weare.common.Utils;
import com.ep.weare.ministry.service.MinistryService;
import com.ep.weare.user.controller.UserController;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
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
public class MinistryRestController {

    private final MinistryService ministryService;
    private final UserController userController;
    private final CommentService commentService;

    public MinistryRestController(MinistryService ministryService, UserController userController,
                                  CommentService commentService) {
        this.ministryService = ministryService;
        this.userController = userController;
        this.commentService = commentService;
    }

    // 사역 게시글 삭제
    @PostMapping("/ministryDelete.do")
    public ResponseEntity<String> deleteQuestion (Model model, HttpSession session,
                                                  @RequestParam int ministryId) {
        userController.updateModelWithUserInfo(model, session);

        commentService.deleteByMinistryId(ministryId);
        ministryService.deleteByMinistryId(ministryId);

        return ResponseEntity.ok("성공적으로 삭제되었습니다.");
    }

    // 사역 게시판 이미지 업로드(제출은 안한 상태)
    @PostMapping(value="/uploadMinistryImage", produces = "application/json")
    public JsonObject uploadQuestionImageFile(@RequestParam("file") MultipartFile multipartFile) {

        JsonObject jsonObject = new JsonObject();

//        String fileRoot = "C:\\weareAttach\\ministryImage\\";	// 로컬용
        String fileRoot = "/sukey0331/tomcat/webapps/ministryImage/";	//저장될 외부 파일 경로
        String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명

        String savedFileName = Utils.getRenameFilename(originalFileName);	//저장될 파일 명

        File targetFile = new File(fileRoot + savedFileName);

        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장
            jsonObject.addProperty("url", "/ministryImage/"+savedFileName);
            jsonObject.addProperty("responseCode", "success");

        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }

        return jsonObject;
    }

}
