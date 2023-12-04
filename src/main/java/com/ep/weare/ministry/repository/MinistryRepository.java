package com.ep.weare.ministry.repository;

import com.ep.weare.ministry.entity.Ministry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MinistryRepository extends JpaRepository<Ministry, Long> {

    Page<Ministry> findByTitleContainingOrContentContaining(String keyword, String keyword1, Pageable pageable);

    Optional<Ministry> findFirstByOrderByMinistryIdDesc();

    Page<Ministry> findByMsteamNameInOrTitleContainingOrContentContaining(List<String> msteamNames, String keyword, String keyword1, Pageable pageable);
}
