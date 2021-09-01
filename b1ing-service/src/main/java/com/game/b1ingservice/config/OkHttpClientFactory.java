package com.game.b1ingservice.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpClientFactory {
    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .callTimeout(120, TimeUnit.SECONDS)
                .build();
        return client;
    }


}