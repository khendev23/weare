package com.ep.weare.post.repository;

import com.ep.weare.post.entity.AnnouncementAttach;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnnounceAttachRepository extends JpaRepository<AnnouncementAttach, Long> {
    Optional<AnnouncementAttach> findFirstByOrderByAnnounceAttachmentIdDesc();

    List<AnnouncementAttach> findByAnnounceId(int announceId);

    void deleteByAnnounceId(int announceId);
}
