package com.game.b1ingservice.service.item;

import com.game.b1ingservice.payload.item.AddItemRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ItemService {
    void addItem(AddItemRequest addItemRequst);
    }


