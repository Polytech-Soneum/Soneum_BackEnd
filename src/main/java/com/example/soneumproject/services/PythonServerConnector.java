package com.example.soneumproject.services;

import com.example.soneumproject.services.UnityFunctionService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PythonServerConnector {
    private final UnityFunctionService unityFunctionService;

    public PythonServerConnector(UnityFunctionService unityFunctionService) {
        this.unityFunctionService = unityFunctionService;
    }

    public String connectToPythonServer(String connectURL, JsonObject requestParam) {
        int responseCode = 0;
        String responseMessage = "";
        List<String> unityFunctionNames = new ArrayList<>();

        try {
            URL url = new URL("http://127.0.0.1:5000/soneum/utils/translate/" + connectURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            connection.setDoOutput(true);
            connection.setDoInput(true);

            byte[] jsonData = requestParam.toString().getBytes(StandardCharsets.UTF_8);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonData);
                os.flush();
            }

            responseCode = connection.getResponseCode();

            InputStream inputStream = responseCode == 201
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            responseMessage = response.toString();

            if (connectURL.equals("voice")) {
                String[] words = responseMessage.replaceAll("[{}\"]", "").replace("[", "").replace("]", "").replace("message: ", "").split(", ");

                for (String word : words) {
                    String function = unityFunctionService.getFunctionName(word.trim());
                    if (function != null) {
                        unityFunctionNames.add(function);
                    }
                }
            }

            reader.close();
            connection.disconnect();
        } catch (IOException error) {
            responseMessage = "Error: " + error.getMessage();
        }

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("code", responseCode);
        jsonResponse.add("message", new Gson().toJsonTree(connectURL.equals("voice") ? unityFunctionNames : responseMessage));

        System.out.println(jsonResponse.toString());

        return jsonResponse.toString();
    }
}
