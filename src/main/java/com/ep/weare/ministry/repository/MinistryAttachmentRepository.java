package com.ep.weare.ministry.repository;

import com.ep.weare.ministry.entity.MinistryAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MinistryAttachmentRepository extends JpaRepository<MinistryAttachment, Long> {

    Optional<MinistryAttachment> findFirstByOrderByMinistryAttachmentIdDesc();

    List<MinistryAttachment> findByMinistryId(int ministryId);

    void deleteByMinistryId(int ministryId);
}
