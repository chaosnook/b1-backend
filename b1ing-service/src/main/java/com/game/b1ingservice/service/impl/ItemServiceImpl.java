package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.payload.item.AddItemRequest;
import com.game.b1ingservice.postgres.entity.Item;
import com.game.b1ingservice.postgres.repository.ItemRepository;
import com.game.b1ingservice.service.item.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.game.b1ingservice.postgres.entity.audit.UserAuditEmbeddable;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    ItemRepository itemRepository;

    @Override
    public void addItem(AddItemRequest addItemRequest) {
        UserAuditEmbeddable audit = new UserAuditEmbeddable();
        audit.setCreatedBy("zong");
        audit.setUpdatedBy("");

        Item item = new Item();
        item.setName(addItemRequest.getName());
        item.setQuantity(addItemRequest.getQuantity());
        item.setCost(addItemRequest.getCost());
        item.setSale(addItemRequest.getSale());
        itemRepository.save(item);
    }
}
