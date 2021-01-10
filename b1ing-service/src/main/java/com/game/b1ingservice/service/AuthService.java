package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.commons.UserPrincipal;

import java.util.Map;

public interface AuthService {

    UserPrincipal authenticator(String username, String password);

    Map<String, String> validate(Map<String, String> headers);

}
