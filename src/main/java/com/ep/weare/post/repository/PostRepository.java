package com.ep.weare.post.repository;

import com.ep.weare.post.dto.AnnounceWithUser;
import com.ep.weare.post.dto.AnnounceWithUserInterface;
import com.ep.weare.post.entity.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Announcement, Long> {

    @Query(value = "SELECT a.*, u.user_name FROM announce a JOIN user u ON a.user_id = u.user_id WHERE a.announce_id = :id", nativeQuery = true)
    Optional<AnnounceWithUserInterface> findAnnounceWithUserById(@Param("id") int id);

    List<Announcement> findByImportantOrderByAnnounceIdDesc(char important);

    Page<Announcement> findByTitleContainingOrContentContaining(String keyword, String keyword1, Pageable pageable);

    List<Announcement> findTop3ByOrderByAnnounceIdDesc();
}
