package com.walkingdead.rickgrimesbot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class ChatGPT {
    @Value("${chatGPTKey}")
    private String chatGPTKey;

    public String sendMessageToChatGPT(String text) throws Exception {

        String url = "https://api.openai.com/v1/chat/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + chatGPTKey);

        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", text);

        JSONArray messages = new JSONArray();
        messages.put(message);

        JSONObject data = new JSONObject();
        data.put("model", "gpt-3.5-turbo");
        data.put("messages", messages);
        data.put("max_tokens", 4000);
        data.put("temperature", 0.7);

        con.setDoOutput(true);
        con.getOutputStream().write(data.toString().getBytes());

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        return (new JSONObject(output).getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content"));
    }

}
