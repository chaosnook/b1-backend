package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
}
