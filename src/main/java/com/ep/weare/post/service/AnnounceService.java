package com.ep.weare.post.service;

import com.ep.weare.post.entity.Announcement;
import com.ep.weare.post.entity.AnnouncementAttach;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AnnounceService {
    List<Announcement> findAllAnnouncement();

    Optional<Announcement> findAnnounceById(int id);

    List<Announcement> findByImportantOrderByAnnounceIdDesc(char important);

    Page<Announcement> findAll(Pageable pageable);

    Announcement saveAnnounce(Announcement announcement);

    void deleteAnnounceById(int announceId);

    Page<Announcement> findByKeywordWithPaging(String keyword, Pageable pageable);

    List<Announcement> findTop3ByOrderByAnnounceIdDesc();

    Optional<AnnouncementAttach> findFirstByOrderByAnnounceAttachmentIdDesc();

    Optional<Announcement> findFirstByOrderByAnnounceIdDesc();

    List<AnnouncementAttach> findById(int announceId);
}