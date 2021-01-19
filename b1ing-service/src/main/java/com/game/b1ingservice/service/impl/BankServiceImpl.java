package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.payload.bank.BankRequest;
import com.game.b1ingservice.postgres.entity.Bank;
import com.game.b1ingservice.postgres.repository.BankRepository;
import com.game.b1ingservice.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BankServiceImpl implements BankService {
    @Autowired
    BankRepository bankRepository;

    @Override
    public void insertBank(BankRequest bankRequest){
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
        bankRepository.save(bank);
    }
}
