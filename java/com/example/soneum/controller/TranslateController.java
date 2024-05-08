package com.example.soneum.controller;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/translate")

/* TODO: 추후 CORS 제어 파일 생성 예정 or 링크 변경 예정  */
@CrossOrigin("http://localhost:3000")
public class TranslateController {
    @GetMapping("/")
    public String translateConnected() {
        System.out.println("env: " + System.getenv());
        return "Connected Ok";
    }

    @GetMapping("/voice")
    public String[] translateVoice(String text) {
        /* TODO: 입력된 텍스트의 단어 분할 및 DB 조회를 통한 Unity 함수 response 예정 */
        return text.split(" ");
    }

    @PostMapping("/sign")
    public String translateSign(@RequestBody Map<String, String> requestBody) {

        String base64Data = requestBody.get("sign_video");
        byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
        String decodedString = new String(decodedBytes);

        return decodedString;
    }

    @GetMapping("/text")
    public String makeVoiceBase64(String text, Boolean selectedGender) {
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            // 입력으로 사용할 텍스트 설정
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

            // 음성 선택 매개변수 설정
            VoiceSelectionParams voice =
                    VoiceSelectionParams.newBuilder()
                            .setLanguageCode("ko-KR") // 언어 코드 설정
                            .setSsmlGender(selectedGender ? SsmlVoiceGender.MALE : SsmlVoiceGender.FEMALE)
                            .build();

            // 반환될 오디오 파일 형식 설정
            AudioConfig audioConfig =
                    AudioConfig.newBuilder()
                            .setAudioEncoding(AudioEncoding.MP3) // Linear16 형식 설정 (Base64로 인코딩하기 위해)
                            .build();

            // Text-to-Speech 요청 수행
            SynthesizeSpeechResponse response =
                    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // 응답에서 오디오 내용 가져오기
            ByteString audioContents = response.getAudioContent();

            // ByteString을 byte 배열로 변환
            byte[] audioBytes = audioContents.toByteArray();

            // Base64로 인코딩하여 반환
            return Base64.getEncoder().encodeToString(audioBytes);

        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
