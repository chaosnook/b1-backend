package com.game.b1ingservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.b1ingservice.payload.amb.*;
import com.game.b1ingservice.postgres.jdbc.WebUserJdbcRepository;
import com.game.b1ingservice.service.AMBService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.game.b1ingservice.commons.Constants.AMB_ERROR;

@RestController
@RequestMapping("api/test")
public class TestController {
    @Autowired
    private WebUserJdbcRepository webUserJdbcRepository;
    @Autowired
    private AMBService ambService;
    @Autowired
    private OkHttpClient client;

    @Autowired
    private ObjectMapper objectMapper;


    private static final okhttp3.MediaType MEDIA_JSON = okhttp3.MediaType.parse("application/json");

    @Value("${agent.b1ing.url}")
    private String urlApi;

    @GetMapping("/testtoken")
    @ResponseBody
    public String testtoken() {
        return "Test Token";
    }


    @PostMapping(value = "/amb/create/{key}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public AmbResponse<CreateUserRes> createUserAMB(@RequestBody CreateUserReq createUserReq, @PathVariable String key) {
        AmbResponse<CreateUserRes> ambResponse = new AmbResponse<>();
        try {
            okhttp3.RequestBody body = okhttp3.RequestBody.create(objectMapper.writeValueAsString(createUserReq), MEDIA_JSON);

            Request request = new Request.Builder()
                    .url(String.format("%s/create/%s", urlApi, key))
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.code() != 200) {
                ambResponse.setCode(AMB_ERROR);
                return ambResponse;
            }

            return objectMapper.readValue(response.body().string(), new TypeReference<AmbResponse<CreateUserRes>>() {
            });

        } catch (Exception e) {
            ambResponse.setCode(AMB_ERROR);
        }
        return ambResponse;
    }


    @PutMapping(value = "/amb/reset-password/{key}/{username}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public AmbResponse resetPasswordAMB(@RequestBody ResetPasswordReq resetPasswordReq,
                                     @PathVariable String key, @PathVariable String username) {

        AmbResponse ambResponse = new AmbResponse<>();
        try {
            okhttp3.RequestBody body = okhttp3.RequestBody.create(objectMapper.writeValueAsString(resetPasswordReq), MEDIA_JSON);
            Request request = new Request.Builder()
                    .url(String.format("%s/reset-password/%s/%s", urlApi, key, username))
                    .method("PUT", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();

            if (response.code() != 200) {
                ambResponse.setCode(AMB_ERROR);
                return ambResponse;
            }

            return objectMapper.readValue(response.body().string(), new TypeReference<AmbResponse>() {
            });
        } catch (Exception e) {
            ambResponse.setCode(AMB_ERROR);
        }
        return ambResponse;

    }

    @PostMapping(value = "/amb/withdraw/{key}/{username}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public AmbResponse<WithdrawRes> withdrawAMB(@RequestBody WithdrawReq withdrawReq,
                                             @PathVariable String key, @PathVariable String username) {
        AmbResponse<WithdrawRes> ambResponse = new AmbResponse<>();
        try {
            okhttp3.RequestBody body = okhttp3.RequestBody.create(objectMapper.writeValueAsString(withdrawReq), MEDIA_JSON);

            Request request = new Request.Builder()
                    .url(String.format("%s/withdraw/%s/%s", urlApi, key, username))
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.code() != 200) {
                ambResponse.setCode(AMB_ERROR);
                return ambResponse;
            }

            return objectMapper.readValue(response.body().string(), new TypeReference<AmbResponse<WithdrawRes>>() {
            });

        } catch (Exception e) {
            ambResponse.setCode(AMB_ERROR);
        }
        return ambResponse;
    }

    @PostMapping(value = "/amb/deposit/{key}/{username}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public AmbResponse<DepositRes> depositAMB(@RequestBody DepositReq depositReq,
                                           @PathVariable String key, @PathVariable String username) {
        AmbResponse<DepositRes> ambResponse = new AmbResponse<>();
        try {
            okhttp3.RequestBody body = okhttp3.RequestBody.create(objectMapper.writeValueAsString(depositReq), MEDIA_JSON);

            Request request = new Request.Builder()
                    .url(String.format("%s/deposit/%s/%s", urlApi, key, username))
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.code() != 200) {
                ambResponse.setCode(AMB_ERROR);
                return ambResponse;
            }

            return objectMapper.readValue(response.body().string(), new TypeReference<AmbResponse<DepositRes>>() {
            });

        } catch (Exception e) {
            ambResponse.setCode(AMB_ERROR);
        }
        return ambResponse;
    }


    @GetMapping(value = "/amb/credit/{key}/{username}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public AmbResponse<GetCreditRes> getCreditAMB(@PathVariable String key, @PathVariable String username) {
        AmbResponse<GetCreditRes> ambResponse = new AmbResponse<>();
        try {
            Request request = new Request.Builder()
                    .url(String.format("%s/credit/%s/%s", urlApi, key, username))
                    .method("GET", null)
                    .build();

            Response response = client.newCall(request).execute();

            if (response.code() != 200) {
                ambResponse.setCode(AMB_ERROR);
                return ambResponse;
            }
            return objectMapper.readValue(response.body().string(), new TypeReference<AmbResponse<GetCreditRes>>() {
            });
        } catch (Exception e) {
            ambResponse.setCode(AMB_ERROR);
        }
        return ambResponse;
    }
}
