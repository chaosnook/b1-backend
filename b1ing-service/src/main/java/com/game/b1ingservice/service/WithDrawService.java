package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.withdraw.WithDrawRequest;
import com.game.b1ingservice.payload.withdraw.WithDrawResponse;

public interface WithDrawService {
    WithDrawResponse withdraw(WithDrawRequest withDrawRequest, String username, String prefix);

}
