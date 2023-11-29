package com.ep.weare.post.service;

import com.ep.weare.post.entity.Announcement;
import com.ep.weare.post.entity.AnnouncementAttach;
import com.ep.weare.post.repository.AnnounceAttachRepository;
import com.ep.weare.post.repository.PostRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AnnounceServiceImpl implements AnnounceService {

    private PostRepository postRepository;
    private AnnounceAttachRepository announceAttachRepository;
    private EntityManager entityManager;

    @Autowired
    public AnnounceServiceImpl(PostRepository postRepository, AnnounceAttachRepository announceAttachRepository, EntityManager entityManager) {
        this.postRepository = postRepository;
        this.announceAttachRepository = announceAttachRepository;
        this.entityManager = entityManager;
    }



    @Override
    public List<Announcement> findAllAnnouncement() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Announcement> findAnnounceById(int id) {
        return postRepository.findById((long) id);
    }

    @Override
    public List<Announcement> findByImportantOrderByAnnounceIdDesc(char important) {
        return postRepository.findByImportantOrderByAnnounceIdDesc(important);
    }

    @Override
    public Page<Announcement> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Announcement saveAnnounce(Announcement announcement) {

        Announcement savedAnnouncement = postRepository.save(announcement);



        return savedAnnouncement;
    }

    @Override
    public void deleteAnnounceById(int announceId) {
        postRepository.deleteById((long) announceId);
    }

    @Override
    public Page<Announcement> findByKeywordWithPaging(String keyword, Pageable pageable) {
        return postRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
    }

    @Override
    public List<Announcement> findTop3ByOrderByAnnounceIdDesc() {
        return postRepository.findTop3ByOrderByAnnounceIdDesc();
    }

    @Override
    public Optional<AnnouncementAttach> findFirstByOrderByAnnounceAttachmentIdDesc() {
        return announceAttachRepository.findFirstByOrderByAnnounceAttachmentIdDesc();
    }

    @Override
    public Optional<Announcement> findFirstByOrderByAnnounceIdDesc() {
        return postRepository.findFirstByOrderByAnnounceIdDesc();
    }

    @Override
    public List<AnnouncementAttach> findById(int announceId) {
        return announceAttachRepository.findByAnnounceId(announceId);
    }


}
