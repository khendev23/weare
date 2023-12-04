package com.ep.weare.executives.controller;

import com.ep.weare.common.Utils;
import com.ep.weare.executives.service.ExecutivesService;
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

@RestController
@Slf4j
public class ExecutivesRestController {

    private final UserController userController;
    private final ExecutivesService executivesService;

    public ExecutivesRestController(UserController userController, ExecutivesService executivesService) {
        this.userController = userController;
        this.executivesService = executivesService;
    }

    // 임원 게시글 삭제
    @PostMapping("/executivesDelete.do")
    public ResponseEntity<String> deleteExecutives (Model model, HttpSession session,
                                                  @RequestParam int executivesId) {
        userController.updateModelWithUserInfo(model, session);

        executivesService.deleteByExecutivesId(executivesId);

        return ResponseEntity.ok("성공적으로 삭제되었습니다.");
    }

    // 사역 게시판 이미지 업로드(제출은 안한 상태)
    @PostMapping(value="/uploadExecutivesImage", produces = "application/json")
    public JsonObject uploadExecutivesImageFile(@RequestParam("file") MultipartFile multipartFile) {

        JsonObject jsonObject = new JsonObject();

        String fileRoot = "C:\\weareAttach\\executivesImage\\";	//저장될 외부 파일 경로
        String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명

        String savedFileName = Utils.getRenameFilename(originalFileName);	//저장될 파일 명

        File targetFile = new File(fileRoot + savedFileName);

        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장
            jsonObject.addProperty("url", "/weare/ministryImage/"+savedFileName);
            jsonObject.addProperty("responseCode", "success");

        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }

        return jsonObject;
    }

}
