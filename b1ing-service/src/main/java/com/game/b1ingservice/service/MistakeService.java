package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.misktake.MistakeReq;
import com.game.b1ingservice.payload.misktake.MistakeSearchListRes;
import com.game.b1ingservice.payload.misktake.MistakeSearchReq;
import com.game.b1ingservice.payload.misktake.MistakeSearchSummaryRes;

import java.util.List;

public interface MistakeService {

    void createMistake(MistakeReq mistakeReq, UserPrincipal principal);

    List<MistakeSearchListRes> findByCriteria(MistakeSearchReq req, UserPrincipal principal);

    MistakeSearchSummaryRes summaryData(List<MistakeSearchListRes> resList);

    void clearLimit();
}
