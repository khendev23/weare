package com.ep.weare.user.dto;

import com.ep.weare.user.entity.Gender;
import com.ep.weare.user.entity.UserCheck;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserSignupDTO {

    private String userId;
    private String userPw;
    private String userName;
    private LocalDate birthday;
    private String email;
    private String phone;
    private Gender gender;
    private String address;
    private String teamName;
    private String msTeamName;
}
