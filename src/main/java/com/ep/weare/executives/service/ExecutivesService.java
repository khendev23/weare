package com.ep.weare.executives.service;

import com.ep.weare.executives.entity.Executives;
import com.ep.weare.executives.entity.ExecutivesAttachment;
import com.ep.weare.ministry.entity.Ministry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ExecutivesService {

    Page<Executives> findExectuviesByCategoriesAndKeywordWithPaging(List<String> categories, String search, Pageable pageable);


    Page<Executives> findExecutivesAll(Pageable pageable);

    Optional<Executives> findFirstByOrderByExecutivesIdDesc();

    Optional<ExecutivesAttachment> findFirstByOrderByExecutivesAttachmentIdDesc();

    void saveExecutives(Executives executives);

    Optional<Executives> findExecutivesById(int id);

    List<ExecutivesAttachment> findById(int executivesId);

    void deleteExecutivesAttachmentById(Integer attachmentId);

    void deleteByExecutivesId(int executivesId);
}
