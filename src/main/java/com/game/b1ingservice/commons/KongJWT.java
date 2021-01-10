package com.game.b1ingservice.commons;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class KongJWT {
    private String next;
    private List<KongJWTData> data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class KongJWTData {
        private String key;
        private String secret;
        private String algorithm;
    }
}
