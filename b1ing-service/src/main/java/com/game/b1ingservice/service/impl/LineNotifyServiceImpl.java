package com.game.b1ingservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.b1ingservice.payload.amb.AmbResponse;
import com.game.b1ingservice.payload.amb.CreateUserRes;
import com.game.b1ingservice.payload.line.LineRes;
import com.game.b1ingservice.service.LineNotifyService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class LineNotifyServiceImpl implements LineNotifyService {

    @Autowired
    private OkHttpClient client;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${line.notify.url}")
    private String LINE_NOTIFY_URL;

    private static final MediaType MEDIA_FORM = MediaType.parse("application/x-www-form-urlencoded");

    @Override
    public LineRes sendLineNotifyMessages(String msg, String token)  {
        LineRes lineRes = new LineRes();
        try {
            RequestBody body = RequestBody.create(String.format("message=%s", msg), MEDIA_FORM);
            Request request = new Request.Builder()
                    .url(LINE_NOTIFY_URL)
                    .method("POST", body)
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                lineRes = objectMapper.readValue(response.body().string(), LineRes.class);
                log.info("line notify {}" , lineRes);
            }
        } catch (Exception e) {
            log.error("getCredit", e);
            lineRes.setMessage(e.getMessage());
            lineRes.setStatus(400);
            log.error("line notify {}" , lineRes);
        }

        return lineRes;
    }


}
