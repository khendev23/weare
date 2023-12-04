package com.ep.weare.comment.controller;

import com.ep.weare.comment.entity.CommentMinistry;
import com.ep.weare.comment.entity.CommentQuestion;
import com.ep.weare.comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
public class CommentRestController {

    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/question/deleteComment.do")
    public ResponseEntity<?> deleteQuestionComment (@RequestParam int commentId) {

        // 삭제 누른 댓글의 commentId를 가져온다.
        Optional<CommentQuestion> commentQuestionOptional = commentService.findById(commentId);

        // 만약 있으면 deleteCk를 o로 만든다.
        if (commentQuestionOptional.isPresent()) {
            CommentQuestion commentQuestion = commentQuestionOptional.get();

            commentQuestion.setDeleteCk("o");

            // DB 수정한다.
            commentService.saveComment(commentQuestion);
        }
        return ResponseEntity.ok("삭제 처리 되었습니다.");
    }

    @PostMapping("/ministry/deleteComment.do")
    public ResponseEntity<?> deleteMinistryComment (@RequestParam int commentId) {

        // 삭제 누른 댓글의 commentId를 가져온다.
        Optional<CommentMinistry> commentMinistryOptional = commentService.findByCommentId(commentId);

        // 만약 있으면 deleteCk를 o로 만든다.
        if (commentMinistryOptional.isPresent()) {
            CommentMinistry commentMinistry = commentMinistryOptional.get();

            commentMinistry.setDeleteCk("o");

            // DB 수정한다.
            commentService.saveMinistryComment(commentMinistry);
        }
        return ResponseEntity.ok("삭제 처리 되었습니다.");
    }

}
