package com.ep.weare.comment.controller;

import com.ep.weare.comment.entity.CommentMinistry;
import com.ep.weare.comment.entity.CommentQuestion;
import com.ep.weare.comment.service.CommentService;
import com.ep.weare.user.entity.UserEntity;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@Slf4j
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 질문 게시판 댓글 등록
    @PostMapping("/question/commentCreate.do")
    public String commentCreate (@ModelAttribute("CommentQuestion") CommentQuestion commentQuestion,
                                 @RequestParam int questionId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<CommentQuestion> commentQuestionOptional = commentService.findFirstByOrderByCommentIdDesc();
        if (commentQuestionOptional.isPresent()) {
            int commentById = commentQuestionOptional.get().getCommentId();
            commentQuestion.setCommentId(commentById + 1);
        } else {
            commentQuestion.setCommentId(1);
        }

        commentQuestion.setQuestionId(questionId);
        commentQuestion.setUserId(username);
        commentQuestion.setCommentLevel(1);
        commentQuestion.setDeleteCk("x");

        commentService.saveComment(commentQuestion);

        return "redirect:/questionDetail?id=" + questionId;
    }

    // 질문 게시판 대댓글 등록
    @PostMapping("/question/replyCreate.do")
    public String replyCreate (@ModelAttribute("CommentQuestion") CommentQuestion commentQuestion,
                                @RequestParam int questionId,
                                @RequestParam int commentRef) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<CommentQuestion> commentQuestionOptional = commentService.findFirstByOrderByCommentIdDesc();

        if (commentQuestionOptional.isPresent()) {
            int commentById = commentQuestionOptional.get().getCommentId();
            commentQuestion.setCommentId(commentById + 1);
        } else {
            commentQuestion.setCommentId(1);
        }

        commentQuestion.setQuestionId(questionId);
        commentQuestion.setUserId(username);
        commentQuestion.setCommentLevel(2);
        commentQuestion.setCommentRef(commentRef);
        commentQuestion.setDeleteCk("x");

        commentService.saveComment(commentQuestion);

        return "redirect:/questionDetail?id=" + questionId;
    }

    // 사역 게시판 댓글 등록
    @PostMapping("/ministry/commentCreate.do")
    public String ministryCommentCreate (@ModelAttribute("CommentMinistry") CommentMinistry commentMinistry,
                                        @RequestParam int ministryId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<CommentMinistry> commentMinistryOptional = commentService.findFirstByOrderByMinistryIdDesc();
        if (commentMinistryOptional.isPresent()) {
            int commentById = commentMinistryOptional.get().getCommentId();
            commentMinistry.setCommentId(commentById + 1);
        } else {
            commentMinistry.setCommentId(1);
        }

        commentMinistry.setMinistryId(ministryId);
        commentMinistry.setUserId(username);
        commentMinistry.setCommentLevel(1);
        commentMinistry.setDeleteCk("x");

        commentService.saveMinistryComment(commentMinistry);

        return "redirect:/ministryDetail?id=" + ministryId;
    }

    // 사역 게시판 대댓글 등록
    @PostMapping("/ministry/replyCreate.do")
    public String ministryReplyCreate (@ModelAttribute("CommentMinistry") CommentMinistry commentMinistry,
                                       @RequestParam int ministryId, @RequestParam int commentRef) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<CommentMinistry> commentMinistryOptional = commentService.findFirstByOrderByMinistryIdDesc();
        if (commentMinistryOptional.isPresent()) {
            int commentById = commentMinistryOptional.get().getCommentId();
            commentMinistry.setCommentId(commentById + 1);
        } else {
            commentMinistry.setCommentId(1);
        }

        commentMinistry.setMinistryId(ministryId);
        commentMinistry.setUserId(username);
        commentMinistry.setCommentLevel(2);
        commentMinistry.setCommentRef(commentRef);
        commentMinistry.setDeleteCk("x");

        commentService.saveMinistryComment(commentMinistry);

        return "redirect:/ministryDetail?id=" + ministryId;

    }

}
