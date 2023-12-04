package com.ep.weare.comment.entity;

import com.ep.weare.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "comment_ministry")
public class CommentMinistry {

    @Id
    private int commentId;
    private int ministryId;
    @Column(name = "user_id") // 논리적인 컬럼 이름 지정
    private String userId;
    private String commentContent;
    private int commentLevel;
    private int commentRef;
    private LocalDateTime commentCreatedAt;
    private String deleteCk;

    @PrePersist
    protected void onCreate() {
        if (commentCreatedAt == null) {
            commentCreatedAt = LocalDateTime.now();
        }
    }

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false) // user_id는 UserEntity 엔티티의 ID에 해당하는 컬럼명으로 수정해야 함
    private UserEntity userEntity;

}
