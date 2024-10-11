package com.example.soneumproject.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.*;

import java.sql.Date;

@Getter
@Setter
public class UserRegisterRequestDTO {

    @NotBlank(message = "아이디가 입력되지 않았습니다.")
    @Size(min = 6, max = 15, message = "아이디는 6~15 글자만 입력 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디는 영어 대/소문자와 숫자만 입력 가능합니다.")
    private String id;

    @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
    @Size(min = 8, max = 15, message = "비밀번호는 8~15 글자만 입력 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]*$", message = "비밀번호는 영어 대/소문자와 숫자, 특수기호만 입력 가능합니다.")
    private String password;

    @NotBlank(message = "이메일이 입력되지 않았습니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|naver\\.com|kakao\\.com)$", message = "사용이 불가능한 메일 주소입니다. gmail.com, naver.com, kakao.com의 이메일 주소를 사용해주세요")
    private String email;

    @NotBlank(message = "닉네임이 입력되지 않았습니다.")
    @Size(min = 1, max = 15, message = "닉네임의 길이는 1 ~ 15글자만 가능합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]*$", message = "닉네임에는 특수기호 사용이 불가능합니다.")
    private String nickname;

    private Integer gender;
    private Integer disabilityStatus;
}
