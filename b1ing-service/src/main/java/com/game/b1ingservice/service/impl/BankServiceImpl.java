package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.bank.BankRequest;
import com.game.b1ingservice.payload.bank.BankResponse;
import com.game.b1ingservice.payload.bankdeposit.UserBankDepositResponse;
import com.game.b1ingservice.payload.bankdeposit.UserTrueWalletResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.Bank;
import com.game.b1ingservice.postgres.entity.Wallet;
import com.game.b1ingservice.postgres.repository.AgentRepository;
import com.game.b1ingservice.postgres.repository.BankRepository;
import com.game.b1ingservice.postgres.repository.WalletRepository;
import com.game.b1ingservice.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BankServiceImpl implements BankService {
    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Override
    public void insertBank(BankRequest bankRequest, UserPrincipal principal) {

        Optional<Agent> agent = agentRepository.findById(principal.getAgentId());

        if (!agent.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        }

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

        bank.setPrefix(agent.get().getPrefix());
        bank.setAgent(agent.get());
        bankRepository.save(bank);
    }

    @Override
    public List<BankResponse> getBank(Long agentId) {
        List<BankResponse> res = new ArrayList<>();
        List<Bank> listBank = bankRepository.findAllByAgent_Id(agentId);

        for (Bank bank : listBank) {
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
        return res;
    }

    @Override
    public void updateBank(Long id, BankRequest bankRequest, Long agentId) {
        Optional<Bank> opt = bankRepository.findByIdAndAgent_Id(id, agentId);
        if (opt.isPresent()) {
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
    public void deleteBank(Long id, Long agentId) {
        Optional<Bank> opt = bankRepository.findByIdAndAgent_Id(id, agentId);
        if (opt.isPresent()) {
            Bank bank = opt.get();
            int bankGroupFrom = bank.getBankGroup();
            int bankOrderFrom = bank.getBankOrder();
            Optional<Bank> opt2 = bankRepository.findFirstByActiveAndBankGroupAndBankOrderAndAgent_IdGreaterThanOrderByBankOrderAsc(true, bankGroupFrom, bankOrderFrom, agentId);

            if (opt2.isPresent()) {
                Bank bankCurrent = opt2.get();
                walletRepository.updateAllBankDeposit(bankCurrent.getId(), bank.getId());
                bank.setDeleteFlag(1);
                bankRepository.save(bank);
            } else {
                Optional<Bank> opt3 = bankRepository.findFirstByActiveAndBankGroupAndAgent_IdGreaterThanOrderByBankGroupAsc(true, bankGroupFrom, agentId);
                if (opt3.isPresent()) {
                    Bank bankCurrent = opt3.get();
                    walletRepository.updateAllBankDeposit(bankCurrent.getId(), bank.getId());
                    bank.setDeleteFlag(1);
                    bankRepository.save(bank);
                }
            }
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_02011);
        }
    }

    @Override
    public UserBankDepositResponse getUserBankDeposit(String username, Long agentId) {
        Wallet wallet = walletRepository.findFirstByUser_UsernameAndUser_Agent_Id(username, agentId);
        if (wallet == null || wallet.getBank() == null || !wallet.getBank().isActive()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_00007);
        }
        UserBankDepositResponse response = new UserBankDepositResponse();
        Bank bank = wallet.getBank();
        response.setBankName(bank.getBankName());
        response.setBankCode(bank.getBankCode());
        response.setBankAccountNo(bank.getBankAccountNo());
        response.setBankAccountName(bank.getBankAccountName());
        return response;
    }

    @Override
    public UserTrueWalletResponse getUserTrueWallet(String username, Long agentId) {
        Wallet wallet = walletRepository.findFirstByUser_UsernameAndUser_Agent_Id(username, agentId);
        if (wallet == null || wallet.getTrueWallet() == null || !wallet.getTrueWallet().isActive()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_00007);
        }
        UserTrueWalletResponse response = new UserTrueWalletResponse();
        response.setPhoneNumber(wallet.getTrueWallet().getPhoneNumber());
        return response;
    }
}
