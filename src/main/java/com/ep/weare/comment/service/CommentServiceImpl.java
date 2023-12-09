package com.ep.weare.comment.service;

import com.ep.weare.comment.dto.CommentCountByMinistry;
import com.ep.weare.comment.dto.CommentCountByQuestionId;
import com.ep.weare.comment.entity.CommentMinistry;
import com.ep.weare.comment.entity.CommentQuestion;
import com.ep.weare.comment.repository.CommentMinistryRepository;
import com.ep.weare.comment.repository.CommentQuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentQuestionRepository commentQuestionRepository;
    private final CommentMinistryRepository commentMinistryRepository;

    public CommentServiceImpl(CommentQuestionRepository commentRepository,
                              CommentMinistryRepository commentMinistryRepository) {
        this.commentQuestionRepository = commentRepository;
        this.commentMinistryRepository = commentMinistryRepository;
    }

    @Override
    public void saveComment(CommentQuestion commentQuestion) {
        commentQuestionRepository.save(commentQuestion);
    }

    @Override
    public List<CommentQuestion> findCommentById(int id) {
        return commentQuestionRepository.findByQuestionIdAndCommentLevelOrderByCommentIdAsc(id, 1);
    }

    @Override
    public List<CommentQuestion> findReplyById(int id) {
        return commentQuestionRepository.findByQuestionIdAndCommentLevelOrderByCommentIdAsc(id, 2);
    }

    @Override
    public Optional<CommentQuestion> findFirstByOrderByCommentIdDesc() {
        return commentQuestionRepository.findFirstByOrderByCommentIdDesc();
    }

    @Override
    @Transactional
    public void deleteByQuestionId(int questionId) {
        commentQuestionRepository.deleteByQuestionId(questionId);
    }

    @Override
    public List<CommentCountByQuestionId> getCommentCountsForQuestions() {
        List<Object[]> rawResult = commentQuestionRepository.countAllComments();
        List<CommentCountByQuestionId> result = rawResult.stream()
                .map(row -> new CommentCountByQuestionId((Integer) row[0], (Long) row[1]))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<CommentCountByMinistry> getCommentCountsForMinistry() {
        List<Object[]> rawResult = commentMinistryRepository.countAllComments();
        List<CommentCountByMinistry> result = rawResult.stream()
                .map(row -> new CommentCountByMinistry((Integer) row[0], (Long) row[1]))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    @Transactional
    public void deleteByMinistryId(int ministryId) {
        commentMinistryRepository.deleteByMinistryId(ministryId);
    }

    @Override
    public List<CommentMinistry> findCommentMinistryById(int id) {
        return commentMinistryRepository.findByMinistryIdAndCommentLevelOrderByCommentIdAsc(id, 1);
    }

    @Override
    public List<CommentMinistry> findReplyMinistryById(int id) {
        return commentMinistryRepository.findByMinistryIdAndCommentLevelOrderByCommentIdAsc(id, 2);
    }

    @Override
    public Optional<CommentQuestion> findById(int commentId) {
        return commentQuestionRepository.findById((long) commentId);
    }

    @Override
    public Optional<CommentMinistry> findFirstByOrderByMinistryIdDesc() {
        return commentMinistryRepository.findFirstByOrderByMinistryIdDesc();
    }

    @Override
    public void saveMinistryComment(CommentMinistry commentMinistry) {
        commentMinistryRepository.save(commentMinistry);
    }

    @Override
    public Optional<CommentMinistry> findByCommentId(int commentId) {
        return commentMinistryRepository.findById((long) commentId);
    }
}
