package com.game.b1ingservice;

import com.game.b1ingservice.payload.amb.DepositReq;
import com.game.b1ingservice.service.AMBService;
import com.game.b1ingservice.utils.AESUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootTest
class B1ingServiceApplicationTests {

    @Autowired
    private AMBService ambService;

	@Test
	void contextLoads() {
//        ambService.getCredit("VBKK000000");
//        ambService.deposit(DepositReq.builder().amount("10.00").build(), "VBKK000000");
	}

}
