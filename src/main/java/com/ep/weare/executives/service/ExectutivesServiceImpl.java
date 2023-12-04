package com.ep.weare.executives.service;

import com.ep.weare.executives.entity.Executives;
import com.ep.weare.executives.entity.ExecutivesAttachment;
import com.ep.weare.executives.repository.ExecutivesAttachmentRepository;
import com.ep.weare.executives.repository.ExecutivesRepository;
import com.ep.weare.ministry.entity.Ministry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExectutivesServiceImpl implements ExecutivesService {

    private final ExecutivesRepository executivesRepository;
    private final ExecutivesAttachmentRepository executivesAttachmentRepository;

    public ExectutivesServiceImpl(ExecutivesRepository executivesRepository, ExecutivesAttachmentRepository executivesAttachmentRepository) {
        this.executivesRepository = executivesRepository;
        this.executivesAttachmentRepository = executivesAttachmentRepository;
    }

    @Override
    public Page<Executives> findExectuviesByCategoriesAndKeywordWithPaging(List<String> categories, String keyword, Pageable pageable) {
        if (categories != null && !categories.isEmpty()) {
            // msteamNames와 keyword를 모두 고려한 쿼리
            return executivesRepository.findByCategoryInOrTitleContainingOrContentContaining(categories, keyword, keyword, pageable);
        } else {
            // msteamNames가 없는 경우에는 keyword만 고려한 쿼리
            return executivesRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
        }
    }

    @Override
    public Page<Executives> findExecutivesAll(Pageable pageable) {
        return executivesRepository.findAll(pageable);
    }

    @Override
    public Optional<Executives> findFirstByOrderByExecutivesIdDesc() {
        return executivesRepository.findFirstByOrderByExecutivesIdDesc();
    }

    @Override
    public Optional<ExecutivesAttachment> findFirstByOrderByExecutivesAttachmentIdDesc() {
        return executivesAttachmentRepository.findFirstByOrderByExecutivesAttachmentIdDesc();
    }

    @Override
    public void saveExecutives(Executives executives) {
        executivesRepository.save(executives);
    }

    @Override
    public Optional<Executives> findExecutivesById(int id) {
        return executivesRepository.findById((long) id);
    }

    @Override
    public List<ExecutivesAttachment> findById(int executivesId) {
        return executivesAttachmentRepository.findByExecutivesId(executivesId);
    }

    @Override
    public void deleteExecutivesAttachmentById(Integer attachmentId) {
        executivesAttachmentRepository.deleteById((long) attachmentId);
    }

    @Override
    public void deleteByExecutivesId(int executivesId) {
        executivesRepository.deleteById((long) executivesId);
    }

}
