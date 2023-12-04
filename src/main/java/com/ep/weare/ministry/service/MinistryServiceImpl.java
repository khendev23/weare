package com.ep.weare.ministry.service;

import com.ep.weare.ministry.entity.Ministry;
import com.ep.weare.ministry.entity.MinistryAttachment;
import com.ep.weare.ministry.repository.MinistryAttachmentRepository;
import com.ep.weare.ministry.repository.MinistryRepository;
import com.ep.weare.ministry.repository.MinistryTeamRepository;
import com.ep.weare.user.entity.MinistryTeam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MinistryServiceImpl implements MinistryService {

    private final MinistryRepository ministryRepository;
    private final MinistryTeamRepository ministryTeamRepository;
    private final MinistryAttachmentRepository ministryAttachmentRepository;

    public MinistryServiceImpl(MinistryRepository ministryRepository,
                               MinistryTeamRepository ministryTeamRepository,
                               MinistryAttachmentRepository ministryAttachmentRepository) {
        this.ministryRepository = ministryRepository;
        this.ministryTeamRepository = ministryTeamRepository;
        this.ministryAttachmentRepository = ministryAttachmentRepository;
    }

    @Override
    public Page<Ministry> findMinistryByKeywordWithPaging(String keyword, Pageable pageable) {
        return ministryRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
    }

    @Override
    public Page<Ministry> findMinistryAll(Pageable pageable) {
        return ministryRepository.findAll(pageable);
    }

    @Override
    public List<MinistryTeam> findAll() {
        return ministryTeamRepository.findAll();
    }

    @Override
    public Optional<Ministry> findFirstByOrderByMinistryIdDesc() {
        return ministryRepository.findFirstByOrderByMinistryIdDesc();
    }

    @Override
    public Optional<MinistryAttachment> findFirstByOrderByMinistryAttachmentIdDesc() {
        return ministryAttachmentRepository.findFirstByOrderByMinistryAttachmentIdDesc();
    }

    @Override
    public void saveMinistry(Ministry ministry) {
        ministryRepository.save(ministry);
    }

    @Override
    public Optional<Ministry> findMinistryById(int id) {
        return ministryRepository.findById((long) id);
    }

    @Override
    public List<MinistryAttachment> findById(int ministryId) {
        return ministryAttachmentRepository.findByMinistryId(ministryId);
    }

    @Override
    public void deleteByMinistryId(int ministryId) {

        ministryAttachmentRepository.deleteByMinistryId(ministryId);
        ministryRepository.deleteById((long) ministryId);
    }

    @Override
    public void deleteMinistryAttachmentById(Integer attachmentId) {
        ministryAttachmentRepository.deleteById((long) attachmentId);
    }

    @Override
    public Page<Ministry> findMinistriesByMsteamNamesAndKeywordWithPaging(List<String> msteamNames, String keyword, Pageable pageable) {
        if (msteamNames != null && !msteamNames.isEmpty()) {
            // msteamNames와 keyword를 모두 고려한 쿼리
            log.info("3번 실행");
            return ministryRepository.findByMsteamNameInOrTitleContainingOrContentContaining(msteamNames, keyword, keyword, pageable);
        } else {
            // msteamNames가 없는 경우에는 keyword만 고려한 쿼리
            log.info("4번 실행");
            return ministryRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
        }
    }


}
