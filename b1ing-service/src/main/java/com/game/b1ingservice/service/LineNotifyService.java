package com.game.b1ingservice.service;


import com.game.b1ingservice.payload.line.LineRes;


public interface LineNotifyService {

    LineRes sendLineNotifyMessages(String msg, String token);

}
