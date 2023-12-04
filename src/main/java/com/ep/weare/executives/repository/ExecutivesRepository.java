package com.ep.weare.executives.repository;

import com.ep.weare.executives.entity.Executives;
import com.ep.weare.ministry.entity.Ministry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExecutivesRepository extends JpaRepository<Executives, Long> {

    Page<Executives> findByCategoryInOrTitleContainingOrContentContaining(List<String> categories, String keyword, String keyword1, Pageable pageable);

    Page<Executives> findByTitleContainingOrContentContaining(String keyword, String keyword1, Pageable pageable);

    Optional<Executives> findFirstByOrderByExecutivesIdDesc();
}
