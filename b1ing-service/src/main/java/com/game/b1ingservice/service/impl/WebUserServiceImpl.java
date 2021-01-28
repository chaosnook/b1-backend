package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.webuser.WebUserRequest;
import com.game.b1ingservice.payload.webuser.WebUserResponse;
import com.game.b1ingservice.payload.webuser.WebUserUpdate;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.postgres.repository.WebUserRepository;
import com.game.b1ingservice.service.WebUserService;
import com.game.b1ingservice.utils.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WebUserServiceImpl implements WebUserService {

    @Autowired
    private WebUserRepository webUserRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseEntity<?> createUser(WebUserRequest req) {

        String tel = req.getTel();
        String prefix = "BNG";
        String username = prefix + tel.substring(3, tel.length()-1);

        WebUser user = new WebUser();
        user.setUsername(username);
        user.setTel(req.getTel());
        user.setPassword(bCryptPasswordEncoder.encode(req.getPassword()));
        user.setBankName(req.getBankName());
        user.setAccountNumber(req.getAccountNumber());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setLine(req.getLine());
        user.setIsBonus(req.getIsBonus());

        webUserRepository.save(user);

        WebUserResponse resp = new WebUserResponse();
        resp.setUsername(username);
        resp.setPassword(req.getPassword());

        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, resp);
    }

    @Override
    public void updateUser(Long id, WebUserUpdate req) {
        Optional<WebUser> opt = webUserRepository.findById(id);
        if(opt.isPresent()) {
            WebUser user = opt.get();
            user.setBankName(req.getBankName());
            user.setAccountNumber(req.getAccountNumber());
            user.setFirstName(req.getFirstName());
            user.setLastName(req.getLastName());
            user.setLine(req.getLine());
            user.setIsBonus(req.getIsBonus());

            webUserRepository.save(user);
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01104);
        }
    }
}
