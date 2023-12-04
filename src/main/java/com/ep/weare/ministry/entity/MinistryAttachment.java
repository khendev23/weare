package com.ep.weare.ministry.entity;

import com.ep.weare.post.entity.Announcement;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ministry_attachment")
public class MinistryAttachment {

    @Id
    private int ministryAttachmentId;
    private int ministryId;
    private String ministryOriginalFilename;
    private String ministryRenamedFilename;

    @ManyToOne
    @JoinColumn(name = "ministryId", insertable = false, updatable = false)
    private Ministry ministry;

    @Override
    public String toString() {
        return "MinistryAttachment{" +
                "ministryAttachmentId=" + ministryAttachmentId +
                ", ministryId=" + ministryId +
                ", ministryOriginalFilename='" + ministryOriginalFilename + '\'' +
                ", ministryRenamedFilename='" + ministryRenamedFilename + '\'' +
                '}';
    }
}

