package com.example.soneumproject.repository;

import com.example.soneumproject.entity.UnityFunction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnityFunctionRepository extends JpaRepository<UnityFunction, Long> {
    UnityFunction findByKoreanWord(String koreanWord);
}