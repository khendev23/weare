package com.ep.weare.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity {

    // 필드
    @Id
    private String userId;
    private String userPw;
    private String userName;
    private LocalDate birthday;
    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    private String address;
    private String rankName;
    private String teamName;
    private String msTeamName;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_check")
    private UserCheck userCheck;

    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    private Authority authority;
}
