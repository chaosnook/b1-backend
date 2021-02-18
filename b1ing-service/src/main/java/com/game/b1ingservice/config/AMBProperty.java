package com.game.b1ingservice.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("agent.b1ing")
public class AMBProperty {

    private String url;

    private Boolean enable;

    private String key;

    private String prefix;

    private String clientname;

}
