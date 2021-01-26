package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.webuser.WebUserRequest;
import com.game.b1ingservice.payload.webuser.WebUserUpdate;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.postgres.repository.WebUserRepository;
import com.game.b1ingservice.service.WebUserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void createUser(WebUserRequest req) {

        String tel = req.getTel();
        String prefix = "BNG";
        String username = prefix + tel.substring(2, tel.length()-1);

        WebUser user = new WebUser();
        user.setUsername(username);
        user.setTel(req.getTel());
        user.setPassword(bCryptPasswordEncoder.encode(req.getPassword()));
        user.setBank_name(req.getBankName());
        user.setAccount_number(req.getAccountNumber());
        user.setFirst_name(req.getFirstName());
        user.setLast_name(req.getLastName());
        user.setLine(req.getLine());
        user.setIs_bonus(req.getIsBonus());

        webUserRepository.save(user);
    }

    @Override
    public void updateUser(Long id, WebUserUpdate req) {
        Optional<WebUser> opt = webUserRepository.findById(id);
        if(opt.isPresent()) {
            WebUser user = opt.get();
            user.setBank_name(req.getBankName());
            user.setAccount_number(req.getAccountNumber());
            user.setFirst_name(req.getFirstName());
            user.setLast_name(req.getLastName());
            user.setLine(req.getLine());
            user.setIs_bonus(req.getIsBonus());

            webUserRepository.save(user);
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01104);
        }
    }
}
