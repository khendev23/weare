package com.ep.weare.post.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "announce")
public class Announcement {

    @Id
    private int announceId;
    private String title;
    private String content;
    private LocalDate announceCreatedAt;
    private String userId;
    private char important;

    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL)
    private List<AnnouncementAttach> attaches;

    @PrePersist
    protected void onCreate() {
        if (announceCreatedAt == null) {
            announceCreatedAt = LocalDate.now();
        }
    }

}
