package com.example.soneumproject.controller;

import com.example.soneumproject.dto.UserLoginRequestDTO;
import com.example.soneumproject.dto.UserRegisterRequestDTO;
import com.example.soneumproject.entity.Users;
import com.example.soneumproject.services.UserService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/id")
    public String idDuplicateCheck(@RequestBody String requestBody) {
        JsonObject jsonResponse = new JsonObject();

        try {
            JsonObject jsonRequest = JsonParser.parseString(requestBody).getAsJsonObject();
            JsonElement userIdElement = jsonRequest.get("user_id");

            if (userIdElement == null || userIdElement.isJsonNull()) {
                jsonResponse.addProperty("code", 400); // Bad Request
                jsonResponse.addProperty("message", "아이디가입력되지 않았습니다");
                return jsonResponse.toString();
            }

            String userId = userIdElement.getAsString();
            boolean exists = userService.checkIdDuplicate(userId);

            jsonResponse.addProperty("code", exists ? 409 : 200);
            jsonResponse.addProperty("message", exists ? "이미 사용 중인 아이디입니다." : "사용 가능한 아이디입니다.");
        } catch (JsonSyntaxException e) {
            jsonResponse.addProperty("code", 400); // Bad Request
            jsonResponse.addProperty("message", "Invalid JSON format.");
        } catch (Exception e) {
            jsonResponse.addProperty("code", 500); // Internal Server Error
            jsonResponse.addProperty("message", "서버 오류가 발생했습니다.");
        }

        return jsonResponse.toString();
    }

    @PostMapping("/login")
    public String userLogin(@RequestBody UserLoginRequestDTO userDto) {
        JsonObject jsonRequest = userService.loginUser(userDto.getId(), userDto.getPassword());

        return jsonRequest.toString();
    }

    @PostMapping("/register")
    public String userRegister(@Valid @RequestBody UserRegisterRequestDTO userDto) {
        JsonObject jsonResponse = new JsonObject();

        try {
            // 사용자 정보를 데이터베이스에 저장
            userService.registerUser(userDto);

            jsonResponse.addProperty("code", 200);
            jsonResponse.addProperty("message", "회원가입 완료");
            return jsonResponse.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.addProperty("code", 500);
            jsonResponse.addProperty("message", "회원가입 중 오류가 발생했습니다.");
            return jsonResponse.toString();
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex) {
        JsonObject jsonResponse = new JsonObject();

        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null) {
            jsonResponse.addProperty("code", 205);
            jsonResponse.addProperty("message", fieldError.getDefaultMessage());
            return jsonResponse.toString();
        }

        jsonResponse.addProperty("code", 500);
        jsonResponse.addProperty("message", "서버 오류");
        return jsonResponse.toString();
    }
}
