package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.amb.*;

public interface AMBService {

    AmbResponse<CreateUserRes> createUser(CreateUserReq createUserReq);

    AmbResponse resetPassword(ResetPasswordReq resetPasswordReq);

    AmbResponse<CreateUserRes> withdraw(WithdrawReq withdrawReq, String username);

    AmbResponse<DepositRes> deposit(DepositReq depositReq, String username);

    AmbResponse<GameStatusRes> getGameStatus(GameStatusReq gameStatusReq);

    AmbResponse<GetCreditRes> getCredit(String username);
}
