package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.AdminUser;
import com.game.b1ingservice.postgres.entity.CalculatorHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculatorHistoryRepository extends JpaRepository<CalculatorHistory, Long>, JpaSpecificationExecutor<CalculatorHistory> {
    int deleteById(long id);
}
