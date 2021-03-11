package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    @Query(value = "select count(1) from point_history where type = 'EARN_POINT' and user_id = ? and deposit_user_id = ?", nativeQuery = true)
    int countByUserAndUserDep(Long user, Long depUser);
}
