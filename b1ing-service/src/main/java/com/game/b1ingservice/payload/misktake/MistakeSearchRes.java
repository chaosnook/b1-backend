package com.game.b1ingservice.payload.misktake;

import lombok.Data;

import java.util.List;

@Data
public class MistakeSearchRes {

    private List<MistakeSearchListRes> list;
    private MistakeSearchSummaryRes summary;
}
