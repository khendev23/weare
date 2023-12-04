package com.ep.weare.post.service;

import com.ep.weare.post.entity.Announcement;
import com.ep.weare.post.entity.AnnouncementAttach;
import com.ep.weare.post.entity.Question;
import com.ep.weare.post.entity.Worship;
import com.ep.weare.post.repository.AnnounceAttachRepository;
import com.ep.weare.post.repository.AnnounceRepository;
import com.ep.weare.post.repository.QuestionRepository;
import com.ep.weare.post.repository.WorshipRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private AnnounceRepository announceRepository;
    private AnnounceAttachRepository announceAttachRepository;
    private WorshipRepository worshipRepository;
    private QuestionRepository questionRepository;

    @Autowired
    public PostServiceImpl(AnnounceRepository announceRepository,
                           AnnounceAttachRepository announceAttachRepository,
                           WorshipRepository worshipRepository,
                           QuestionRepository questionRepository) {

        this.announceRepository = announceRepository;
        this.announceAttachRepository = announceAttachRepository;
        this.worshipRepository = worshipRepository;
        this.questionRepository = questionRepository;

    }



    @Override
    public List<Announcement> findAllAnnouncement() {
        return announceRepository.findAll();
    }

    @Override
    public Optional<Announcement> findAnnounceById(int id) {
        return announceRepository.findById((long) id);
    }

    @Override
    public List<Announcement> findByImportantOrderByAnnounceIdDesc(char important) {
        return announceRepository.findByImportantOrderByAnnounceIdDesc(important);
    }

    @Override
    public Page<Announcement> findAll(Pageable pageable) {
        return announceRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Announcement saveAnnounce(Announcement announcement) {

        Announcement savedAnnouncement = announceRepository.save(announcement);



        return savedAnnouncement;
    }

    @Override
    @Transactional
    public void deleteAnnounceById(int announceId) {
        announceAttachRepository.deleteByAnnounceId(announceId);
        announceRepository.deleteById((long) announceId);
    }

    @Override
    public Page<Announcement> findByKeywordWithPaging(String keyword, Pageable pageable) {
        return announceRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
    }

    @Override
    public List<Announcement> findTop3ByOrderByAnnounceIdDesc() {
        return announceRepository.findTop3ByOrderByAnnounceIdDesc();
    }

    @Override
    public Optional<AnnouncementAttach> findFirstByOrderByAnnounceAttachmentIdDesc() {
        return announceAttachRepository.findFirstByOrderByAnnounceAttachmentIdDesc();
    }

    @Override
    public Optional<Announcement> findFirstByOrderByAnnounceIdDesc() {
        return announceRepository.findFirstByOrderByAnnounceIdDesc();
    }

    @Override
    public List<AnnouncementAttach> findById(int announceId) {
        return announceAttachRepository.findByAnnounceId(announceId);
    }

    @Override
    public List<Worship> findAllWorship() {
        return worshipRepository.findAll();
    }

    @Override
    public Page<Worship> findWorshipByKeywordWithPaging(String keyword, Pageable pageable) {
        return worshipRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
    }

    @Override
    public Page<Worship> findWorshipAll(Pageable pageable) {
        return worshipRepository.findAll(pageable);
    }

    @Override
    public Optional<Worship> findFirstByOrderByWorshipIdDesc() {
        return worshipRepository.findFirstByOrderByWorshipIdDesc();
    }

    @Override
    public void save(Worship worship) {
        worshipRepository.save(worship);
    }

    @Override
    public Optional<Worship> findWorshipById(int id) {
        return worshipRepository.findById((long) id);
    }

    @Override
    public Page<Question> findQuestionByKeywordWithPaging(String keyword, Pageable pageable) {
        return questionRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
    }

    @Override
    public Page<Question> findQuestionAll(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    @Override
    public Optional<Question> findFirstByOrderByQuestionIdDesc() {
        return questionRepository.findFirstByOrderByQuestionIdDesc();
    }

    @Override
    public void saveQuestion(Question question) {
        questionRepository.save(question);
    }

    @Override
    public Optional<Question> findQuestionById(int id) {
        return questionRepository.findById((long) id);
    }

    @Override
    public void deleteQuestionById(int questionId) {
        questionRepository.deleteById((long) questionId);
    }

    @Override
    public void deleteAnnounceAttachmentById(Integer attachmentId) {
        announceAttachRepository.deleteById((long) attachmentId);
    }

}
