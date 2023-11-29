package com.ep.weare.post.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "announce_attachment")
public class AnnouncementAttach {

    @Id
    private int announceAttachmentId;
    private int announceId;
    private String announceOriginalFilename;
    private String announceRenamedFilename;

    @ManyToOne
    @JoinColumn(name = "announceId", insertable = false, updatable = false)
    private Announcement announcement;

    @Override
    public String toString() {
        return "AnnouncementAttach{" +
                "announceAttachmentId=" + announceAttachmentId +
                ", announceId=" + announceId +
                ", announceOriginalFilename='" + announceOriginalFilename + '\'' +
                ", announceRenamedFilename='" + announceRenamedFilename + '\'' +
                '}';
    }
}
