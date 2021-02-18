package com.game.b1ingservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.b1ingservice.config.AMBProperty;
import com.game.b1ingservice.payload.amb.*;
import com.game.b1ingservice.service.AMBService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AMBServiceImpl implements AMBService {

    @Autowired
    private OkHttpClient client;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AMBProperty ambProperty;

    private static MediaType MEDIA_JSON = MediaType.parse("application/json");

    @Override
    public AmbResponse<CreateUserRes> createUser(CreateUserReq createUserReq) {
        AmbResponse<CreateUserRes> res = new AmbResponse<>();
        try {
            String signature = String.format("%s:%s:%s", createUserReq.getMemberLoginName(), createUserReq.getMemberLoginPass(), ambProperty.getPrefix());
            createUserReq.setSignature(DigestUtils.md5Hex(signature));

            RequestBody body = RequestBody.create(objectMapper.writeValueAsString(createUserReq), MEDIA_JSON);

            Request request = new Request.Builder()
                    .url(String.format("%s/create/%s", ambProperty.getUrl(), ambProperty.getKey()))
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            ResponseBody responseBody = response.body();

        } catch (Exception e) {
            res.setCode(9999);
        }

        return null;
    }

    @Override
    public AmbResponse resetPassword(ResetPasswordReq resetPasswordReq, String username) {
        try {
            String signature = String.format("%s:%s", resetPasswordReq.getPassword(), ambProperty.getPrefix());
            resetPasswordReq.setSignature(signature);
            RequestBody body = RequestBody.create(objectMapper.writeValueAsString(resetPasswordReq), MEDIA_JSON);
            Request request = new Request.Builder()
                    .url(String.format("%s/reset-password/%s/%s", ambProperty.getUrl(), ambProperty.getKey(), username))
                    .method("PUT", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public AmbResponse<CreateUserRes> withdraw(WithdrawReq withdrawReq, String username) {
        try {
            String signature = String.format("%s:%s:%s", withdrawReq.getAmount(), username, ambProperty.getPrefix());
            withdrawReq.setSignature(DigestUtils.md5Hex(signature));
            RequestBody body = RequestBody.create(objectMapper.writeValueAsString(withdrawReq), MEDIA_JSON);
            Request request = new Request.Builder()
                    .url(String.format("%s/withdraw/%s/%s", ambProperty.getUrl(), ambProperty.getKey(), username))
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public AmbResponse<DepositRes> deposit(DepositReq depositReq, String username) {
        try {
            String signature = String.format("%s:%s:%s", depositReq.getAmount(), username, ambProperty.getPrefix());
            depositReq.setSignature(DigestUtils.md5Hex(signature));
            RequestBody body = RequestBody.create(objectMapper.writeValueAsString(depositReq), MEDIA_JSON);
            Request request = new Request.Builder()
                    .url(String.format("%s/deposit/%s/%s", ambProperty.getUrl(), ambProperty.getKey(), username))
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            log.info("deposit {} : {}", response, response.body().string());
        } catch (Exception e) {
            log.error("deposit", e);
        }
        return null;
    }

    @Override
    public AmbResponse<GameStatusRes> getGameStatus(GameStatusReq gameStatusReq) {
        try {

            String signature = String.format("%s:%s", ambProperty.getPrefix(), ambProperty.getClientname());
            gameStatusReq.setSignature(DigestUtils.md5Hex(signature));
            RequestBody body = RequestBody.create(objectMapper.writeValueAsString(gameStatusReq), MEDIA_JSON);
            Request request = new Request.Builder()
                    .url(String.format("%s/gameStatus/%s", ambProperty.getUrl(), ambProperty.getKey()))
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public AmbResponse<GetCreditRes> getCredit(String username) {
        try {
            Request request = new Request.Builder()
                    .url(String.format("%s/credit/%s/%s", ambProperty.getUrl(), ambProperty.getKey(), username))
                    .method("GET", null)
                    .build();

            Response response = client.newCall(request).execute();

            log.info("get credit {} : {}", response, response.body().string());
        } catch (Exception e) {
            log.error("getCredit", e);
        }
        return null;
    }


}
