package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.WinLoseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface WinLoseHistoryRepository extends JpaRepository<WinLoseHistory, Long>, JpaSpecificationExecutor<WinLoseHistory> {


    WinLoseHistory findFirstByUserIdAndLastDateBetweenOrderByIdDesc(Long id, Date start, Date end);

    WinLoseHistory findFirstByUserIdOrderByIdDesc(Long id);
}
