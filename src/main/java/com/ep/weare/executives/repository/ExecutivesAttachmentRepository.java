package com.ep.weare.executives.repository;

import com.ep.weare.executives.entity.ExecutivesAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExecutivesAttachmentRepository extends JpaRepository<ExecutivesAttachment, Long> {

    Optional<ExecutivesAttachment> findFirstByOrderByExecutivesAttachmentIdDesc();

    List<ExecutivesAttachment> findByExecutivesId(int executivesId);
}
