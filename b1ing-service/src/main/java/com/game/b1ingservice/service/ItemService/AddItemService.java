package com.game.b1ingservice.service.ItemService;

import com.game.b1ingservice.payload.items.AddItemRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AddItemService {
    static ResponseEntity<?> addItem(String id, String name, String quantity, String cost, String sale);
}
