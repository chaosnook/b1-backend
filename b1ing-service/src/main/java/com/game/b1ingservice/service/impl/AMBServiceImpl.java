package com.game.b1ingservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.b1ingservice.payload.amb.*;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.service.AMBService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.game.b1ingservice.commons.Constants.AMB_ERROR;

@Slf4j
@Service
public class AMBServiceImpl implements AMBService {

    @Autowired
    private OkHttpClient client;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${agent.b1ing.url}")
    private String urlApi;

    private static final MediaType MEDIA_JSON = MediaType.parse("application/json");

    @Override
    public AmbResponse<CreateUserRes> createUser(CreateUserReq createUserReq, Agent agent) {
        AmbResponse<CreateUserRes> ambResponse = new AmbResponse<>();
        try {
            String signature = String.format("%s:%s:%s", createUserReq.getMemberLoginName(), createUserReq.getMemberLoginPass(), agent.getPrefix());
            createUserReq.setSignature(DigestUtils.md5Hex(signature));

            RequestBody body = RequestBody.create(objectMapper.writeValueAsString(createUserReq), MEDIA_JSON);

            Request request = new Request.Builder()
                    .url(String.format("%s/create/%s", urlApi, agent.getKey()))
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            log.info("createUser {} : {}", createUserReq.getMemberLoginName(), response);

            if (response.code() != 200) {
                ambResponse.setCode(AMB_ERROR);
                return ambResponse;
            }
            return objectMapper.readValue(response.body().string(), new TypeReference<AmbResponse<CreateUserRes>>() {
            });
        } catch (Exception e) {
            log.error("getCredit", e);
            ambResponse.setCode(AMB_ERROR);
        }
        return ambResponse;
    }

    @Override
    public AmbResponse resetPassword(ResetPasswordReq resetPasswordReq, String username, Agent agent) {
        AmbResponse ambResponse = new AmbResponse<>();
        try {
            String signature = String.format("%s:%s", resetPasswordReq.getPassword(), agent.getPrefix());
            resetPasswordReq.setSignature(signature);
            RequestBody body = RequestBody.create(objectMapper.writeValueAsString(resetPasswordReq), MEDIA_JSON);
            Request request = new Request.Builder()
                    .url(String.format("%s/reset-password/%s/%s", urlApi, agent.getKey(), username))
                    .method("PUT", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();

            log.info("getCredit {} : {}", username, response);

            if (response.code() != 200) {
                ambResponse.setCode(AMB_ERROR);
                return ambResponse;
            }

            return objectMapper.readValue(response.body().string(), new TypeReference<AmbResponse>() {
            });

        } catch (Exception e) {
            log.error("resetPassword", e);
            ambResponse.setCode(AMB_ERROR);
        }
        return ambResponse;
    }

    @Override
    public AmbResponse<CreateUserRes> withdraw(WithdrawReq withdrawReq, String username, Agent agent) {
        AmbResponse<CreateUserRes> ambResponse = new AmbResponse<>();
        try {
            String signature = String.format("%s:%s:%s", withdrawReq.getAmount(), username, agent.getPrefix());
            withdrawReq.setSignature(DigestUtils.md5Hex(signature));
            RequestBody body = RequestBody.create(objectMapper.writeValueAsString(withdrawReq), MEDIA_JSON);
            Request request = new Request.Builder()
                    .url(String.format("%s/withdraw/%s/%s", urlApi, agent.getKey(), username))
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            log.info("withdraw {} : {}", username, response);

            if (response.code() != 200) {
                ambResponse.setCode(AMB_ERROR);
                return ambResponse;
            }
            return objectMapper.readValue(response.body().string(), new TypeReference<AmbResponse<CreateUserRes>>() {
            });
        } catch (Exception e) {
            log.error("withdraw", e);
            ambResponse.setCode(AMB_ERROR);
        }
        return ambResponse;
    }

    @Override
    public AmbResponse<DepositRes> deposit(DepositReq depositReq, String username, Agent agent) {
        AmbResponse<DepositRes> ambResponse = new AmbResponse<>();
        try {
            String signature = String.format("%s:%s:%s", depositReq.getAmount(), username, agent.getPrefix());
            depositReq.setSignature(DigestUtils.md5Hex(signature));
            RequestBody body = RequestBody.create(objectMapper.writeValueAsString(depositReq), MEDIA_JSON);
            Request request = new Request.Builder()
                    .url(String.format("%s/deposit/%s/%s", urlApi, agent.getKey(), username))
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            log.info("deposit {} : {}", username, response);
            if (response.code() != 200) {
                ambResponse.setCode(AMB_ERROR);
                return ambResponse;
            }

            return objectMapper.readValue(response.body().string(), new TypeReference<AmbResponse<DepositRes>>() {
            });
        } catch (Exception e) {
            log.error("deposit", e);
            ambResponse.setCode(AMB_ERROR);
        }
        return ambResponse;
    }

    @Override
    public AmbResponse<GameStatusRes> getGameStatus(GameStatusReq gameStatusReq, Agent agent) {
        AmbResponse<GameStatusRes> ambResponse = new AmbResponse<>();
        try {
            String signature = String.format("%s:%s", agent.getPrefix(), agent.getClientName());
            gameStatusReq.setSignature(DigestUtils.md5Hex(signature));
            RequestBody body = RequestBody.create(objectMapper.writeValueAsString(gameStatusReq), MEDIA_JSON);
            Request request = new Request.Builder()
                    .url(String.format("%s/gameStatus/%s", urlApi, agent.getKey()))
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();

            log.info("getGameStatus {} : {}", gameStatusReq.getUsername(), response);
            if (response.code() != 200) {
                ambResponse.setCode(AMB_ERROR);
                return ambResponse;
            }

            return objectMapper.readValue(response.body().string(), new TypeReference<AmbResponse<GameStatusRes>>() {
            });
        } catch (Exception e) {
            log.error("getGameStatus", e);
            ambResponse.setCode(AMB_ERROR);
        }
        return ambResponse;
    }

    @Override
    public AmbResponse<GetCreditRes> getCredit(String username, Agent agent) {
        AmbResponse<GetCreditRes> ambResponse = new AmbResponse<>();
        try {
            Request request = new Request.Builder()
                    .url(String.format("%s/credit/%s/%s", urlApi, agent.getKey(), username))
                    .method("GET", null)
                    .build();

            Response response = client.newCall(request).execute();

            log.info("getCredit {} : {}", username, response);

            if (response.code() != 200) {
                ambResponse.setCode(AMB_ERROR);
                return ambResponse;
            }
            return objectMapper.readValue(response.body().string(), new TypeReference<AmbResponse<GetCreditRes>>() {
            });
        } catch (Exception e) {
            log.error("getCredit", e);
            ambResponse.setCode(AMB_ERROR);
        }
        return ambResponse;
    }


}
