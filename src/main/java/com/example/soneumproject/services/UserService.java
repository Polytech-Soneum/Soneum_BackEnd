package com.example.soneumproject.services;

import com.example.soneumproject.config.JwtTokenUtil;
import com.example.soneumproject.dto.UserRegisterRequestDTO;
import com.example.soneumproject.entity.Grade;
import com.example.soneumproject.entity.Users;
import com.example.soneumproject.repository.UserRepository;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public boolean checkIdDuplicate(String userId) {
        return userRepository.existsById(userId);
    }

    public JsonObject loginUser(String userID, String rawPassword) {
        JsonObject jsonResponse = new JsonObject();
        try {
            Optional<Users> usersOptional = userRepository.findByUserID(userID);

            if(usersOptional.isPresent()) {
                Users users = usersOptional.get();

                if(passwordEncoder.matches(rawPassword, users.getUserPassword())) {
                    users.setUserRecentLogin(LocalDateTime.now());
                    userRepository.save(users);

                    String token = jwtTokenUtil.generateToken(
                            users.getUserID(),
                            users.getUserNickname(),
                            users.getUserGender(),
                            users.getUserGrade(),
                            users.getUserDisabilityStatus()
                    );

                    jsonResponse.addProperty("code", 200);
                    jsonResponse.addProperty("message", token); // 클라이언트에 토큰을 전달
                } else {
                    jsonResponse.addProperty("code", 401);
                    jsonResponse.addProperty("message", "비밀번호가 일치하지 않습니다");
                }
            } else {
                jsonResponse.addProperty("code", 404);
                jsonResponse.addProperty("message", "일치하는 회원 정보가 없습니다");
            }
        } catch (Exception e) {
            jsonResponse.addProperty("code", 500);
            jsonResponse.addProperty("message", "서버 오류");
        }

        return jsonResponse;
    }

    public void registerUser(UserRegisterRequestDTO userDTO) {
        Users user = new Users();
        user.setUserID(userDTO.getId());
        user.setUserPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUserNickname(userDTO.getNickname());
        user.setUserEmail(userDTO.getEmail());
        user.setUserGrade(Grade.NORMAL_USER); // 고정된 값
        user.setUserGender(userDTO.getGender());
        user.setUserGender(userDTO.getGender());
        user.setUserDisabilityStatus(userDTO.getDisabilityStatus());
        user.setUserRecentLogin(LocalDateTime.now());

        userRepository.save(user);
    }
}
