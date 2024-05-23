package com.example.soneumproject.controller;

import com.example.soneumproject.services.UnityFunctionService;
import com.google.cloud.texttospeech.v1.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/translate")

/* TODO: 추후 CORS 제어 파일 생성 예정 or 링크 변경 예정  */
@CrossOrigin(origins = {"http://localhost:3000"})
public class TranslateController {
    @Autowired
    private UnityFunctionService unityFunctionService;

    @GetMapping("/")
    public String translateConnected() {
        System.out.println("env: " + System.getenv());
        return "Connected Ok";
    }

    @GetMapping("/voice")
    public String translateVoice(@RequestParam("text") String text) {
        String responseMessage = "";
        List<String> unityFunctionNames = new ArrayList<>();
        int responseCode = 0;

        try {
            URL url = new URL("http://127.0.0.1:5000/soneum/utils/translate/voice");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept","application/json");

            connection.setDoOutput(true);
            connection.setDoInput(true);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("voice_text", text);
            byte[] jsonData = jsonObject.toString().getBytes(StandardCharsets.UTF_8);

            OutputStream os = connection.getOutputStream();
            os.write(jsonData);
            os.flush();
            os.close();

            responseCode = connection.getResponseCode();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            responseMessage = response.toString();

            String[] words = responseMessage.replaceAll("[{}\"]", "").replace("[", "").replace("]", "").replace("message: ", "").split(", ");
            System.out.println(Arrays.toString(words));

            for (String word : words) {
                String function = unityFunctionService.getFunctionName(word.trim());
                if (function != null) {
                    unityFunctionNames.add(function);
                }
            }
        } catch (IOException error) {
            error.printStackTrace();
        }

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("code", responseCode);
        jsonResponse.add("message", new Gson().toJsonTree(unityFunctionNames));

        return jsonResponse.toString();
    }

    @PostMapping("/sign")
    public String translateSign(@RequestParam(value = "sign_video", required = false) String video_data) {
        String responseMessage = "";
        int responseCode = 0;

        try {
            byte[] bytes = Base64.getDecoder().decode(video_data);

            URL url = new URL("http://127.0.0.1:5000/soneum/utils/translate/sign");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("video_data", Base64.getEncoder().encodeToString(bytes));  // encode file bytes to Base64 string
            byte[] jsonData = jsonObject.toString().getBytes(StandardCharsets.UTF_8);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonData);
                os.flush();
            }

            responseCode = connection.getResponseCode();
            InputStream inputStream = responseCode == 201
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                responseMessage = response.toString();
            }

            connection.disconnect();
        } catch (IOException error) {
            responseMessage = "Error: " + error.getMessage();
        }

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("code", responseCode);
        jsonResponse.add("message", new Gson().toJsonTree(responseMessage));

        return jsonResponse.toString();
    }

    @GetMapping("/text")
    public String makeVoiceBase64(@RequestParam("text") String text, @RequestParam("selectedGender") Boolean selectedGender) {
        // NOTE: 번역 완료된 텍스트와 성별을 선택하여 클릭 시 음성 합성 실행 - 구현 완료 / FrontEnd 테스트 완료
        
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
