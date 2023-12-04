package com.ep.weare.comment.service;

import com.ep.weare.comment.dto.CommentCountByMinistry;
import com.ep.weare.comment.dto.CommentCountByQuestionId;
import com.ep.weare.comment.entity.CommentMinistry;
import com.ep.weare.comment.entity.CommentQuestion;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    void saveComment(CommentQuestion commentQuestion);

    List<CommentQuestion> findCommentById(int id);

    List<CommentQuestion> findReplyById(int id);

    Optional<CommentQuestion> findFirstByOrderByCommentIdDesc();

    void deleteByQuestionId(int questionId);

    List<CommentCountByQuestionId> getCommentCountsForQuestions();

    List<CommentCountByMinistry> getCommentCountsForMinistry();

    void deleteByMinistryId(int ministryId);

    List<CommentMinistry> findCommentMinistryById(int id);

    List<CommentMinistry> findReplyMinistryById(int id);

    Optional<CommentQuestion> findById(int commentId);

    Optional<CommentMinistry> findFirstByOrderByMinistryIdDesc();

    void saveMinistryComment(CommentMinistry commentMinistry);

    Optional<CommentMinistry> findByCommentId(int commentId);
}
