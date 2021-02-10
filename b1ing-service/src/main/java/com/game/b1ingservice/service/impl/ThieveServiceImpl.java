package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.thieve.ThieveRequest;
import com.game.b1ingservice.payload.thieve.ThieveResponse;
import com.game.b1ingservice.payload.thieve.ThieveUpdateRequest;
import com.game.b1ingservice.postgres.entity.Thieve;
import com.game.b1ingservice.postgres.repository.ThieveRepository;
import com.game.b1ingservice.service.ThieveService;
import com.game.b1ingservice.specification.SearchThieveSpecification;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class ThieveServiceImpl implements ThieveService {
    @Autowired
    ThieveRepository thieveRepository;

    @Override
    public ResponseEntity<?> getThieve(Long id) {
        Optional<Thieve> opt = thieveRepository.findById(id);
        if(opt.isPresent()){
            Thieve thieve = opt.get();
            ThieveResponse response = new ThieveResponse();
            response.setName(thieve.getName());
            response.setBankName(thieve.getBankName());
            response.setBankAccount(thieve.getBankAccount());
            return  ResponseEntity.ok(opt.get());
        }
        throw new ErrorMessageException(Constants.ERROR.ERR_01005);
    }


    @Override
    public void addThieve(ThieveRequest thieveRequest){
        Thieve thieve = new Thieve();
        thieve.setName(thieveRequest.getName());
        thieve.setBankName(thieveRequest.getBankName());
        thieve.setBankAccount(thieveRequest.getBankAccount());
        thieveRepository.save(thieve);
    }

    @Override
    public void updateThieve(ThieveUpdateRequest thieveUpdateRequest) {
        Optional<Thieve> opt = thieveRepository.findById(thieveUpdateRequest.getId());
        if (opt.isPresent()) {
            Thieve thieve = opt.get();

            thieve.setName(thieveUpdateRequest.getName());
            thieve.setBankName(thieveUpdateRequest.getBankName());
            thieve.setBankAccount(thieveUpdateRequest.getBankAccount());
            thieveRepository.save(thieve);
        } else {
        throw new ErrorMessageException(Constants.ERROR.ERR_01005);
        }
    }

    @Override
    public void deleteThieve(Long id) {
        Optional<Thieve> opt = thieveRepository.findById(id);
        if(opt.isPresent()) {
            Thieve thieve = opt.get();
            thieve.setDeleteFlag(1);
            thieveRepository.save(thieve);
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01005);
        }
    }


    @Override
    public Page<ThieveResponse> findByCriteria(Specification<Thieve> specification, Pageable pageable){
      return thieveRepository.findAll(specification, pageable).map(converter);
    }
    Function<Thieve, ThieveResponse> converter = thieve -> {
     ThieveResponse thieveResponse = new ThieveResponse();
     thieveResponse.setId(thieve.getId());
     thieveResponse.setName(thieve.getName());
     thieveResponse.setBankName(thieve.getBankName());
     thieveResponse.setBankAccount(thieve.getBankAccount());

     Map<String, Object> configMap = new HashMap<>();
     return  thieveResponse;
    };



}
