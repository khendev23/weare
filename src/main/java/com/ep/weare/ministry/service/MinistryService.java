package com.ep.weare.ministry.service;

import com.ep.weare.ministry.entity.Ministry;
import com.ep.weare.ministry.entity.MinistryAttachment;
import com.ep.weare.user.entity.MinistryTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MinistryService {

    Page<Ministry> findMinistryByKeywordWithPaging(String search, Pageable pageable);

    Page<Ministry> findMinistryAll(Pageable pageable);

    List<MinistryTeam> findAll();

    Optional<Ministry> findFirstByOrderByMinistryIdDesc();

    Optional<MinistryAttachment> findFirstByOrderByMinistryAttachmentIdDesc();

    void saveMinistry(Ministry ministry);

    Optional<Ministry> findMinistryById(int id);

    List<MinistryAttachment> findById(int ministryId);

    void deleteByMinistryId(int ministryId);

    void deleteMinistryAttachmentById(Integer attachmentId);

    Page<Ministry> findMinistriesByMsteamNamesAndKeywordWithPaging(List<String> msteamNames, String search, Pageable pageable);
}
