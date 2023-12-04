package com.ep.weare.comment.repository;

import com.ep.weare.comment.entity.CommentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentQuestionRepository extends JpaRepository<CommentQuestion, Long> {

    Optional<CommentQuestion> findFirstByOrderByCommentIdDesc();

    List<CommentQuestion> findByQuestionIdAndCommentLevelOrderByCommentIdAsc(int id, int commentLevel);

    void deleteByQuestionId(int questionId);


    // 모든 질문에 대한 댓글 수를 가져오는 쿼리 메소드
    @Query(value = "SELECT cq.question_id as questionId, COUNT(cq.question_id) as count " +
            "FROM comment_question cq " +
            "GROUP BY cq.question_id", nativeQuery = true)
    List<Object[]> countAllComments();
}
