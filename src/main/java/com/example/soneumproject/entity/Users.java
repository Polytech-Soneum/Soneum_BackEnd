package com.example.soneumproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
public class Users {
    @Id
    @Column(name="user_id")
    private String userID;

    @Column(name="user_password")
    private String userPassword;

    @Column(name="user_nickname")
    private String userNickname;

    @Column(name="user_email")
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name="user_grade")
    private Grade userGrade;

    @Column(name="user_gender")
    private Integer userGender;

    @Column(name="user_disability_status")
    private Integer userDisabilityStatus;

    @Column(name="user_recent_login")
    private LocalDateTime userRecentLogin;
}
