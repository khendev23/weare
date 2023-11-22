package com.ep.weare.post.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    @PrePersist
    protected void onCreate() {
        if (announceCreatedAt == null) {
            announceCreatedAt = LocalDate.now();
        }
    }

}
