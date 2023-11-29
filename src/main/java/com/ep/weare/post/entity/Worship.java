package com.ep.weare.post.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "post_worship")
public class Worship {

    @Id
    private int worshipId;
    private String userId;
    private String title;
    private String content;
    private LocalDate worshipDate;
    private String link;
    private LocalDate worshipCreatedAt;

    @PrePersist
    protected void onCreate() {
        if (worshipCreatedAt == null) {
            worshipCreatedAt = LocalDate.now();
        }
    }
}
