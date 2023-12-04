package com.ep.weare.executives.entity;

import com.ep.weare.ministry.entity.MinistryAttachment;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "post_executives")
public class Executives {

    @Id
    private int executivesId;
    private String title;
    private String content;
    private String userId;
    private String category;
    private LocalDate executivesCreatedAt;

    @OneToMany(mappedBy = "executives", cascade = CascadeType.ALL)
    private List<ExecutivesAttachment> attachments;

    @PrePersist
    protected void onCreate() {
        if (executivesCreatedAt == null) {
            executivesCreatedAt = LocalDate.now();
        }
    }

}
