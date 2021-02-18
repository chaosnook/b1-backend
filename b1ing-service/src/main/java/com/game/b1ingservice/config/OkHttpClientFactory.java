package com.game.b1ingservice.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OkHttpClientFactory {
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }


}