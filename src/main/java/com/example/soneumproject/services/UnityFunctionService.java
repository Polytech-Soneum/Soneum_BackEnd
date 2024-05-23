package com.example.soneumproject.services;

import com.example.soneumproject.entity.UnityFunction;
import com.example.soneumproject.repository.UnityFunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnityFunctionService {

    @Autowired
    private UnityFunctionRepository unityFunctionRepository;

    public String getFunctionName(String koreanWord) {
        UnityFunction unityFunction = unityFunctionRepository.findByKoreanWord(koreanWord);
        return unityFunction != null ? unityFunction.getFunctionName() : null;
    }
}
