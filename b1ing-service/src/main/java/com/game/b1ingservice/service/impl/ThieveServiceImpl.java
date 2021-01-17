package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.thieve.ThieveRequest;
import com.game.b1ingservice.payload.thieve.ThieveResponse;
import com.game.b1ingservice.postgres.entity.Thieve;
import com.game.b1ingservice.postgres.repository.ThieveRepository;
import com.game.b1ingservice.service.ThieveService;
import com.game.b1ingservice.utils.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ThieveServiceImpl implements ThieveService {
    @Autowired
    ThieveRepository thieveRepository;

    @Override
    public ResponseEntity<?> getThieve(String name) {
        Optional<Thieve> opt = thieveRepository.findByName(name);
        if(opt.isPresent()){
            Thieve thieve = opt.get();
            ThieveResponse response = new ThieveResponse();
            response.setId(thieve.getId());
            response.setName(thieve.getName());
            response.setBank_name(thieve.getBank_name());
            response.setBank_account(thieve.getBank_account());
            return  ResponseEntity.ok(opt.get());
        }
        throw new ErrorMessageException(Constants.ERROR.ERR_01004);
    }


    @Override
    public void addThieve(ThieveRequest thieveRequest){
        Thieve thieve = new Thieve();
        thieve.setName(thieveRequest.getName());
        thieve.setBank_name(thieveRequest.getBank_name());
        thieve.setBank_account(thieveRequest.getBank_account());
        thieveRepository.save(thieve);
    }

}
