package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository  extends JpaRepository<Item, String>, JpaSpecificationExecutor<Item> {

}
