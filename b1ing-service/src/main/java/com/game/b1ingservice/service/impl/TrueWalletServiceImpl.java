package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.truewallet.TrueWalletRequest;
import com.game.b1ingservice.payload.truewallet.TrueWalletResponse;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.TrueWallet;
import com.game.b1ingservice.postgres.repository.AgentRepository;
import com.game.b1ingservice.postgres.repository.TrueWalletRepository;
import com.game.b1ingservice.postgres.repository.WalletRepository;
import com.game.b1ingservice.service.TrueWalletService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrueWalletServiceImpl implements TrueWalletService {

    @Autowired
    private TrueWalletRepository trueWalletRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public void insertTrueWallet(TrueWalletRequest req, UserPrincipal principal) {
        Optional<Agent> agent = agentRepository.findByPrefix(principal.getPrefix());

        if (!agent.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        }

        TrueWallet truewallet = new TrueWallet();
        truewallet.setPhoneNumber(req.getPhoneNumber());
        truewallet.setName(req.getName());
        truewallet.setPassword(req.getPassword());
        truewallet.setBankGroup(req.getBankGroup());
        truewallet.setBotIp(req.getBotIp());
        truewallet.setNewUserFlag(req.isNewUserFlag());
        truewallet.setActive(req.isActive());
        truewallet.setPrefix(principal.getPrefix());
        truewallet.setAgent(agent.get());

        trueWalletRepository.save(truewallet);
    }

    @Override
    public List<TrueWalletResponse> getTrueWallet(UserPrincipal principal) {
        List<TrueWalletResponse> resp = new ArrayList<>();
        List<TrueWallet> listTrueWallet = trueWalletRepository.findAllByPrefix(principal.getPrefix(), Sort.by(Sort.Direction.ASC, "id"));

        for (TrueWallet truewallet : listTrueWallet) {
            TrueWalletResponse truewalletResp = new TrueWalletResponse();
            truewalletResp.setId(truewallet.getId());
            truewalletResp.setPhoneNumber(truewallet.getPhoneNumber());
            truewalletResp.setName(truewallet.getName());
            truewalletResp.setBankGroup(truewallet.getBankGroup());
            truewalletResp.setBotIp(truewallet.getBotIp());
            truewalletResp.setNewUserFlag(truewallet.isNewUserFlag());
            truewalletResp.setActive(truewallet.isActive());
            truewalletResp.setPrefix(truewallet.getPrefix());
            truewalletResp.setVersion(truewallet.getVersion());
            truewalletResp.setCreatedBy(truewallet.getAudit().getCreatedBy());
            truewalletResp.setUpdatedBy(truewallet.getAudit().getUpdatedBy());
            truewalletResp.setCreatedDate(truewallet.getCreatedDate());
            truewalletResp.setUpdatedDate(truewallet.getUpdatedDate());
            resp.add(truewalletResp);
        }

        return resp;
    }

    @Override
    public void updateTrueWallet(Long id, TrueWalletRequest req, UserPrincipal principal) {
        Optional<TrueWallet> opt = trueWalletRepository.findFirstByIdAndPrefix(id, principal.getPrefix());
        if (opt.isPresent()) {
            TrueWallet trueWallet = opt.get();
            trueWallet.setPhoneNumber(req.getPhoneNumber());
            trueWallet.setName(req.getName());
            if (!StringUtils.isEmpty(req.getPassword())) {
                trueWallet.setPassword(req.getPassword());
            }
            trueWallet.setBankGroup(req.getBankGroup());
            trueWallet.setBotIp(req.getBotIp());
            trueWallet.setNewUserFlag(req.isNewUserFlag());
            trueWallet.setActive(req.isActive());
            trueWalletRepository.save(trueWallet);
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01104);
        }
    }

    @Override
    public void deleteTrueWallet(Long id, UserPrincipal principal) {
        Optional<TrueWallet> opt = trueWalletRepository.findFirstByIdAndPrefix(id, principal.getPrefix());
        if (opt.isPresent()) {
            TrueWallet trueWallet = opt.get();
            trueWallet.setDeleteFlag(1);
            trueWalletRepository.save(trueWallet);

            int bankGroupFrom = trueWallet.getBankGroup();
            Optional<TrueWallet> opt2 = trueWalletRepository.findFirstByActiveAndBankGroupGreaterThanOrderByBankGroupAsc(true, bankGroupFrom);
            if(opt2.isPresent()) {
                TrueWallet trueWalletCurrent = opt2.get();
                walletRepository.updateAllTrueWalletDeposit(trueWalletCurrent.getId(), trueWallet.getId());
            } else {
                walletRepository.updateAllTrueWalletDeposit(null, trueWallet.getId());
            }

        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01104);
        }
    }
}
