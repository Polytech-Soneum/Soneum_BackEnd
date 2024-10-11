package com.example.soneumproject.dto;

import lombok.Getter;

@Getter
public class AuthResponseDTO {
    private String token;

    public AuthResponseDTO(String token) {
        this.token = token;
    }
}
