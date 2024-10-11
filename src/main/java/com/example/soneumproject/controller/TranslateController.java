package com.example.soneumproject.controller;

import com.example.soneumproject.services.PythonServerConnector;
import com.example.soneumproject.services.UnityFunctionService;
import com.google.cloud.texttospeech.v1.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/translate")
public class TranslateController {
    @Autowired
    private UnityFunctionService unityFunctionService;

    @PostMapping("/voice")
    public String translateVoice(@RequestParam(value = "text", required = false) String text) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("voice_text", text);

        PythonServerConnector serverConnector = new PythonServerConnector(unityFunctionService);

        return serverConnector.connectToPythonServer("voice", jsonObject);
    }

    @PostMapping("/sign")
    public String translateSign(@RequestParam(value = "sign_video", required = false) String video_data) throws IOException {
        byte[] bytes = Base64.getDecoder().decode(video_data);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("video_data", Base64.getEncoder().encodeToString(bytes));

        PythonServerConnector serverConnector = new PythonServerConnector(unityFunctionService);

        return serverConnector.connectToPythonServer("sign", jsonObject);
    }

    @GetMapping("/text")
    public String makeVoiceBase64(@RequestParam("text") String text, @RequestParam("selectedGender") Boolean selectedGender) {
        // NOTE: 번역 완료된 텍스트와 성별을 선택하여 클릭 시 음성 합성 실행
        
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            // 입력으로 사용할 텍스트 설정
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

            // 음성 선택 매개변수 설정기능 - TODO: 음성의 다른 방식이 있는지 확인 예정
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
