package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.amb.*;
import com.game.b1ingservice.postgres.entity.Agent;

public interface AMBService {

    AmbResponse<CreateUserRes> createUser(CreateUserReq createUserReq, Agent agent);

    AmbResponse resetPassword(ResetPasswordReq resetPasswordReq, String username, Agent agent);

    AmbResponse<WithdrawRes> withdraw(WithdrawReq withdrawReq, String username, Agent agent);

    AmbResponse<DepositRes> deposit(DepositReq depositReq, String username, Agent agent);

    AmbResponse<GameStatusRes> getGameStatus(GameStatusReq gameStatusReq, Agent agent);

    AmbResponse<GetCreditRes> getCredit(String username, Agent agent);
}
