package com.game.b1ingservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.amb.*;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.Config;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.postgres.repository.AgentRepository;
import com.game.b1ingservice.postgres.repository.ConfigRepository;
import com.game.b1ingservice.postgres.repository.WebUserRepository;
import com.game.b1ingservice.service.AMBService;
import com.game.b1ingservice.utils.AESUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.game.b1ingservice.commons.Constants.AGENT_CONFIG.*;
import static com.game.b1ingservice.commons.Constants.AGENT_CONFIG_TYPE.AMB_CONFIG;
import static com.game.b1ingservice.commons.Constants.AMB_ERROR;

@Slf4j
@Service
public class AMBServiceImpl implements AMBService {

    @Autowired
    private WebUserRepository webUserRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private OkHttpClient client;

    @Autowired
    private ObjectMapper objectMapper;

    private static final MediaType MEDIA_JSON = MediaType.parse("application/json");

    @Override
    public AmbResponse<CreateUserRes> createUser(CreateUserReq createUserReq, Agent agent) {
        AmbResponse<CreateUserRes> ambResponse = new AmbResponse<>();
        try {
            String signature = String.format("%s:%s:%s", createUserReq.getMemberLoginName(), createUserReq.getMemberLoginPass(), agent.getCompanyName().toLowerCase());
            createUserReq.setSignature(DigestUtils.md5Hex(signature));

            RequestBody body = RequestBody.create(objectMapper.writeValueAsString(createUserReq), MEDIA_JSON);

            Request request = new Request.Builder()
                    .url(String.format("%s/create/%s", agent.getUrlEndpoint(), agent.getKey()))
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                log.info("createUser {} : {}", createUserReq.getMemberLoginName(), response);

                if (response.code() != 200) {
                    ambResponse.setCode(AMB_ERROR);
                    return ambResponse;
                }

                String res = response.body().string();
                log.info("createUser response {} ", res);
                return objectMapper.readValue(res, new TypeReference<AmbResponse<CreateUserRes>>() {
                });
            }
        } catch (Exception e) {
            log.error("createUser", e);
            ambResponse.setCode(AMB_ERROR);
        }
        return ambResponse;
    }

    @Override
    public AmbResponse resetPassword(ResetPasswordReq resetPasswordReq, String username, Agent agent) {
        AmbResponse ambResponse = new AmbResponse<>();
        try {
            String signature = String.format("%s:%s", resetPasswordReq.getPassword(), agent.getCompanyName().toLowerCase());
            resetPasswordReq.setSignature(DigestUtils.md5Hex(signature));
            RequestBody body = RequestBody.create(objectMapper.writeValueAsString(resetPasswordReq), MEDIA_JSON);
            Request request = new Request.Builder()
                    .url(String.format("%s/reset-password/%s/%s", agent.getUrlEndpoint(), agent.getKey(), username))
                    .method("PUT", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            try (Response response = client.newCall(request).execute()) {

                log.info("resetPassword {} : {}", username, response);

                if (response.code() != 200) {
                    ambResponse.setCode(AMB_ERROR);
                    return ambResponse;
                }

                return objectMapper.readValue(response.body().string(), AmbResponse.class);
            }
        } catch (Exception e) {
            log.error("resetPassword", e);
            ambResponse.setCode(AMB_ERROR);
        }
        return ambResponse;
    }

    @Override
    public AmbResponse<WithdrawRes> withdraw(WithdrawReq withdrawReq, String username, Agent agent) {
        AmbResponse<WithdrawRes> ambResponse = new AmbResponse<>();
        try {
            String signature = String.format("%s:%s:%s", withdrawReq.getAmount(), username, agent.getCompanyName().toLowerCase());
            withdrawReq.setSignature(DigestUtils.md5Hex(signature));
            RequestBody body = RequestBody.create(objectMapper.writeValueAsString(withdrawReq), MEDIA_JSON);
            Request request = new Request.Builder()
                    .url(String.format("%s/withdraw/%s/%s", agent.getUrlEndpoint(), agent.getKey(), username))
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                log.info("AMB withdraw {} : {}", username, response);

                if (response.code() != 200) {
                    ambResponse.setCode(AMB_ERROR);
                    return ambResponse;
                }
                return objectMapper.readValue(response.body().string(), new TypeReference<AmbResponse<WithdrawRes>>() {
                });
            }
        } catch (Exception e) {
            log.error("AMB withdraw : ", e);
            ambResponse.setCode(AMB_ERROR);
        }
//        finally {
//            lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_WITHDRAW,username, withdrawReq.getAmount()) ,
//                    agent.getLineToken());
//        }
        return ambResponse;
    }

    @Override
    public AmbResponse<DepositRes> deposit(DepositReq depositReq, String username, Agent agent) {
        AmbResponse<DepositRes> ambResponse = new AmbResponse<>();
        try {
            String signature = String.format("%s:%s:%s", depositReq.getAmount(), username, agent.getCompanyName().toLowerCase());
            depositReq.setSignature(DigestUtils.md5Hex(signature));
            RequestBody body = RequestBody.create(objectMapper.writeValueAsString(depositReq), MEDIA_JSON);
            Request request = new Request.Builder()
                    .url(String.format("%s/deposit/%s/%s", agent.getUrlEndpoint(), agent.getKey(), username))
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {

                log.info("deposit {} : {}", username, response);
                if (response.code() != 200) {
                    ambResponse.setCode(AMB_ERROR);
                    return ambResponse;
                }

                return objectMapper.readValue(response.body().string(), new TypeReference<AmbResponse<DepositRes>>() {
                });
            }
        } catch (Exception e) {
            log.error("AMB deposit : ", e);
            ambResponse.setCode(AMB_ERROR);
        }
        return ambResponse;
    }

    @Override
    public AmbResponse<GameStatusRes> getGameStatus(GameStatusReq gameStatusReq, Agent agent) {
        AmbResponse<GameStatusRes> ambResponse = new AmbResponse<>();
        try {
            String signature = String.format("%s:%s", agent.getCompanyName().toLowerCase(), agent.getClientName());
            gameStatusReq.setSignature(DigestUtils.md5Hex(signature));
            RequestBody body = RequestBody.create(objectMapper.writeValueAsString(gameStatusReq), MEDIA_JSON);
            Request request = new Request.Builder()
                    .url(String.format("%s/gameStatus/%s", agent.getUrlEndpoint(), agent.getKey()))
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            try (Response response = client.newCall(request).execute()) {

                log.info("getGameStatus {} : {}", gameStatusReq.getUsername(), response);
                if (response.code() != 200) {
                    ambResponse.setCode(AMB_ERROR);
                    return ambResponse;
                }

                return objectMapper.readValue(response.body().string(), new TypeReference<AmbResponse<GameStatusRes>>() {
                });
            }
        } catch (Exception e) {
            log.error("getGameStatus", e);
            ambResponse.setCode(AMB_ERROR);
        }
        return ambResponse;
    }

    @Override
    public AmbResponse<WinLoseResponse> getWinLose(WinLoseReq winLoseReq, Agent agent) {
        AmbResponse<WinLoseResponse> ambResponse = new AmbResponse<>();
        try {
            Request request = new Request.Builder()
                    .url(String.format("%s/winLose/%s/%s/%s", agent.getUrlEndpoint(), agent.getKey(), winLoseReq.getUsernameAmb(), winLoseReq.getDepositRef()))
                    .method("GET", null)
                    .build();

            try (Response response = client.newCall(request).execute()) {

                log.info("getWinLose {} : {}", winLoseReq, response);

                if (response.code() != 200) {
                    ambResponse.setCode(AMB_ERROR);
                    return ambResponse;
                }

                String res = response.body().string();
                log.info("getWinLose response {} ", res);
                return objectMapper.readValue(res, new TypeReference<AmbResponse<WinLoseResponse>>() {
                });
            }
        } catch (Exception e) {
            log.error("getCredit", e);
            ambResponse.setCode(AMB_ERROR);
        }

        return ambResponse;
    }

    @Override
    public AmbResponse<GetCreditRes> getCredit(String username, Agent agent) {
        AmbResponse<GetCreditRes> ambResponse = new AmbResponse<>();
        try {
            Request request = new Request.Builder()
                    .url(String.format("%s/credit/%s/%s", agent.getUrlEndpoint(), agent.getKey(), username))
                    .method("GET", null)
                    .build();

            try (Response response = client.newCall(request).execute()) {

                log.info("getCredit {} : {}", username, response);

                if (response.code() != 200) {
                    ambResponse.setCode(AMB_ERROR);
                    return ambResponse;
                }
                return objectMapper.readValue(response.body().string(), new TypeReference<AmbResponse<GetCreditRes>>() {
                });
            }
        } catch (Exception e) {
            log.error("getCredit", e);
            ambResponse.setCode(AMB_ERROR);
        }
        return ambResponse;
    }

    @Override
    public GameLinkRes getGameLink(String username, String prefix) {

        Optional<Agent> agent = agentRepository.findByPrefix(prefix);

        if (!agent.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        }

        Optional<WebUser> opt = webUserRepository.findByUsernameAndAgent(username, agent.get());
        if (!opt.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_00007);
        }

        String url = "";
        String urlMobile = "";
        String urlUser = agent.get().getWebsite();
        String hash = "";

        List<Config> configs = configRepository.findAllByTypeAndAgent(AMB_CONFIG, agent.get());
        for (Config conf : configs) {
            if (URL_AMB_GAME.equals(conf.getParameter())) {
                url = conf.getValue();
            } else if (URL_AMB_MOBILE_GAME.equals(conf.getParameter())) {
                urlMobile = conf.getValue();
            } else if (AMB_HASH.equals(conf.getParameter())) {
                hash = conf.getValue();
            }
        }

        String password = AESUtils.decrypt(opt.get().getPassword());

        String urlGame = String.format("%s?username=%s&password=%s&url=%s/#?prefix=%s&hash=%s", url,
                opt.get().getUsernameAmb(), password, urlUser, agent.get().getClientName(), hash);

        String urlMobileGame = String.format("%s?username=%s&password=%s&url=%s/#?prefix=%s&hash=%s", urlMobile,
                opt.get().getUsernameAmb(), password, urlUser, agent.get().getClientName(), hash);

        GameLinkRes gameLinkRes = new GameLinkRes();
        gameLinkRes.setUrlWebProduct(urlGame);
        gameLinkRes.setUrlMobileProduct(urlMobileGame);
        return gameLinkRes;
    }

}
