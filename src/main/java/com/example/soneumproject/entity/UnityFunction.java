package com.example.soneumproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "unity_function_table")
public class UnityFunction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "function_number")
    private int functionNumber;

    @Column(name = "function_name", nullable = false)
    private String functionName;



    @Column(name = "korean_word", nullable = false)
    private String koreanWord;

    @Column(name = "edit_date", nullable = false)
    private Date editDate;

    @Column(name = "editor", nullable = false)
    private String editor;
}