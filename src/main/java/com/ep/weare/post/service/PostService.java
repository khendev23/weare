package com.ep.weare.post.service;

import com.ep.weare.post.entity.Announcement;
import com.ep.weare.post.entity.AnnouncementAttach;
import com.ep.weare.post.entity.Question;
import com.ep.weare.post.entity.Worship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostService {
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

    List<Worship> findAllWorship();

    Page<Worship> findWorshipByKeywordWithPaging(String search, Pageable pageable);

    Page<Worship> findWorshipAll(Pageable pageable);

    Optional<Worship> findFirstByOrderByWorshipIdDesc();

    void save(Worship worship);

    Optional<Worship> findWorshipById(int id);

    Page<Question> findQuestionByKeywordWithPaging(String search, Pageable pageable);

    Page<Question> findQuestionAll(Pageable pageable);

    Optional<Question> findFirstByOrderByQuestionIdDesc();

    void saveQuestion(Question question);

    Optional<Question> findQuestionById(int id);

    void deleteQuestionById(int questionId);

    void deleteAnnounceAttachmentById(Integer attachmentId);
}
