package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.bank.BankRequest;
import com.game.b1ingservice.payload.bank.BankResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.entity.Bank;
import com.game.b1ingservice.postgres.repository.BankRepository;
import com.game.b1ingservice.service.BankService;
import com.game.b1ingservice.utils.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BankServiceImpl implements BankService {
    @Autowired
    BankRepository bankRepository;

    @Override
    public void insertBank(BankRequest bankRequest, UserPrincipal principal){
        Bank bank = new Bank();
        bank.setBankCode(bankRequest.getBankCode());
        bank.setBankType(bankRequest.getBankType());
        bank.setBankName(bankRequest.getBankName());
        bank.setBankAccountName(bankRequest.getBankAccountName());
        bank.setBankAccountNo(bankRequest.getBankAccountNo());
        bank.setUsername(bankRequest.getUsername());
        bank.setPassword(bankRequest.getPassword());
        bank.setBankOrder(bankRequest.getBankOrder());
        bank.setBankGroup(bankRequest.getBankGroup());
        bank.setBotIp(bankRequest.getBotIp());
        bank.setNewUserFlag(bankRequest.isNewUserFlag());
        bank.setActive(bankRequest.isActive());
        bank.setPrefix(principal.getPrefix());
        bankRepository.save(bank);
    }

    @Override
    public ResponseEntity<?> getBank(){
        List<BankResponse> res = new ArrayList<>();
        List<Bank> listBank = bankRepository.findAll();
//        if(listBank.isEmpty()) {
//            throw new ErrorMessageException(Constants.ERROR.ERR_02010);
//        }
           for(Bank bank: listBank){
            BankResponse bankResponse = new BankResponse();

            bankResponse.setId(bank.getId());
            bankResponse.setBankCode(bank.getBankCode());
            bankResponse.setBankType(bank.getBankType());
            bankResponse.setBankName(bank.getBankName());
            bankResponse.setBankAccountName(bank.getBankAccountName());
            bankResponse.setBankAccountNo(bank.getBankAccountNo());
            bankResponse.setUsername(bank.getUsername());
            bankResponse.setBankOrder(bank.getBankOrder());
            bankResponse.setBankGroup(bank.getBankGroup());
            bankResponse.setBotIp(bank.getBotIp());
            bankResponse.setNewUserFlag(bank.isNewUserFlag());
            bankResponse.setActive(bank.isActive());
            bankResponse.setVersion(bank.getVersion());
            bankResponse.setCreatedDate(bank.getCreatedDate());
            bankResponse.setUpdatedDate(bank.getUpdatedDate());
            bankResponse.setCreatedBy(bank.getAudit().getCreatedBy());
            bankResponse.setUpdatedBy(bank.getAudit().getUpdatedBy());

            res.add(bankResponse);
        }
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, res);
    }

    @Override
    public void updateBank(Long id, BankRequest bankRequest){
        Optional<Bank> opt = bankRepository.findById(id);
        if(opt.isPresent()){
            Bank bank = opt.get();
            bank.setBankCode(bankRequest.getBankCode());
            bank.setBankType(bankRequest.getBankType());
            bank.setBankName(bankRequest.getBankName());
            bank.setBankAccountName(bankRequest.getBankAccountName());
            bank.setBankAccountNo(bankRequest.getBankAccountNo());
            bank.setUsername(bankRequest.getUsername());
            if (StringUtils.isNotEmpty(bankRequest.getPassword()))
                bank.setPassword(bankRequest.getPassword());
            bank.setBankOrder(bankRequest.getBankOrder());
            bank.setBankGroup(bankRequest.getBankGroup());
            bank.setBotIp(bankRequest.getBotIp());
            bank.setNewUserFlag(bankRequest.isNewUserFlag());
            bank.setActive(bankRequest.isActive());
            bankRepository.save(bank);
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_02011);
        }
    }

    @Override
    public void deleteBank(Long id){
        Optional<Bank> opt = bankRepository.findById(id);
        if(opt.isPresent()){
            Bank bank = opt.get();
            bank.setDeleteFlag(1);
            bankRepository.save(bank);
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_02011);
        }
    }
}
