package com.example.demo.service;

import com.google.gson.Gson;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new Gson();

    @Override
    public boolean authenticate(String username, String password) {
        String json = gson.toJson(Map.of("username", username, "password", password));
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/auth/login")
                .post(body)
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
