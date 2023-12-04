package com.ep.weare.ministry.entity;

import com.ep.weare.post.entity.AnnouncementAttach;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "post_ministry")
public class Ministry {

    @Id
    private int ministryId;
    private String title;
    private String content;
    private String userId;
    private String msteamName;
    private LocalDate ministryCreatedAt;

    @OneToMany(mappedBy = "ministry", cascade = CascadeType.ALL)
    private List<MinistryAttachment> attachments;

    @PrePersist
    protected void onCreate() {
        if (ministryCreatedAt == null) {
            ministryCreatedAt = LocalDate.now();
        }
    }

}
