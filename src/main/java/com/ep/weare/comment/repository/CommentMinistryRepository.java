package com.ep.weare.comment.repository;

import com.ep.weare.comment.entity.CommentMinistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentMinistryRepository extends JpaRepository<CommentMinistry, Long> {

    // 모든 질문에 대한 댓글 수를 가져오는 쿼리 메소드
    @Query(value = "SELECT cm.ministry_id as ministryId, COUNT(cm.ministry_id) as count " +
            "FROM comment_ministry cm " +
            "GROUP BY cm.ministry_id", nativeQuery = true)
    List<Object[]> countAllComments();

    void deleteByMinistryId(int ministryId);

    List<CommentMinistry> findByMinistryIdAndCommentLevelOrderByCommentIdAsc(int id, int i);

    Optional<CommentMinistry> findFirstByOrderByMinistryIdDesc();
}
