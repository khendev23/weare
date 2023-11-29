package com.ep.weare.post.repository;

import com.ep.weare.post.entity.Worship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorshipRepository extends JpaRepository<Worship, Long> {

    Page<Worship> findByTitleContainingOrContentContaining(String keyword, String keyword1, Pageable pageable);

    Optional<Worship> findFirstByOrderByWorshipIdDesc();

}
