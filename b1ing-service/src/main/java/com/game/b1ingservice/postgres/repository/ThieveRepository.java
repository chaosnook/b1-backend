package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Thieve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThieveRepository extends JpaRepository<Thieve, Long>, JpaSpecificationExecutor<Thieve> {
    Optional<Thieve> findById(Long id);
    Optional<Thieve> findByName(String name);
}
