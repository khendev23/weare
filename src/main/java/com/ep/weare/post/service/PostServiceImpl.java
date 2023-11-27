package com.ep.weare.post.service;

import com.ep.weare.post.dto.AnnounceWithUser;
import com.ep.weare.post.entity.Announcement;
import com.ep.weare.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
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
    public Announcement saveAnnounce(Announcement announcement) {
        return postRepository.save(announcement);
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


}
