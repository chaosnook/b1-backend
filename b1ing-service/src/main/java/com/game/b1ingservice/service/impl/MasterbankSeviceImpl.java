package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.MasterBank.MasterBankResponse;
import com.game.b1ingservice.postgres.entity.MasterBank;
import com.game.b1ingservice.postgres.repository.MasterBankRepository;
import com.game.b1ingservice.service.MasterBankService;
import com.game.b1ingservice.utils.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MasterbankSeviceImpl implements MasterBankService {

    @Autowired
    MasterBankRepository masterBankRepository;

    @Override
    public ResponseEntity<?> getMasterBank() {
        List<MasterBankResponse> res = new ArrayList<>();
        List<MasterBank> listMasterBank = masterBankRepository.findAll();

        for (MasterBank masterBank : listMasterBank) {
            MasterBankResponse masterBankResponse = new MasterBankResponse();


            masterBankResponse.setBankName(masterBank.getBankName());
            masterBankResponse.setBankCode(masterBank.getBankCode());
            res.add(masterBankResponse);

        }

        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, res);

    }

}


