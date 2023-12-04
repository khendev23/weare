package com.ep.weare.post.repository;

import com.ep.weare.post.entity.Question;
import com.ep.weare.post.entity.Worship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findByTitleContainingOrContentContaining(String keyword, String keyword1, Pageable pageable);

    Optional<Question> findFirstByOrderByQuestionIdDesc();
}
