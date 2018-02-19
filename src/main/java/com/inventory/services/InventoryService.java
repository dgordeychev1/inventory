package com.inventory.services;

import com.inventory.db.InventoryItem;
import com.inventory.db.InventoryRepository;
import com.inventory.controllers.dto.InventoryAdjustmentResponse;
import com.inventory.controllers.dto.InventoryRequest;
import com.inventory.exceptions.ItemNotFoundException;
import com.inventory.exceptions.UnknownEventTypeException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InventoryService {
    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public InventoryAdjustmentResponse updateInventory(InventoryRequest inventoryRequest) {
        if (inventoryRequest.getType().equals(InventoryEventType.inventory_adjustment.toString())) {
            return adjustInventory(inventoryRequest.getAdjustment(), inventoryRequest.getItemId());
        } else if (inventoryRequest.getType().equals(InventoryEventType.full_sync.toString())) {
            return updateInventory(inventoryRequest.getAdjustment(), inventoryRequest.getItemId());
        }
        throw new UnknownEventTypeException();
    }

    @SneakyThrows
    public Integer getItemCount(String itemId) {
        Optional<Integer> count = repository.getItemCount(itemId);
        if (count.isPresent()) {
            return count.get();
        }
        throw new ItemNotFoundException();
    }

    @SneakyThrows
    private InventoryAdjustmentResponse adjustInventory(Integer adjustment, String itemId) {

        Optional<InventoryItem> res = repository.getItemById(itemId);
        if (!res.isPresent()) {
            throw new ItemNotFoundException();
        }

        InventoryItem item = res.get();
        int oldCount = item.getCount();
        item.setCount(item.getCount() + adjustment);
        repository.save(item);

        return generateResponse(item, oldCount);
    }

    @SneakyThrows
    private InventoryAdjustmentResponse updateInventory(Integer adjustment, String itemId) {
        InventoryItem item = null;
        int oldCount = 0;
        Optional<InventoryItem> res = repository.getItemById(itemId);
        if (res.isPresent()) {
            item = res.get();
            oldCount = item.getCount();
            item.setCount(adjustment);
        } else {
            item = InventoryItem.builder()
                    .itemId(itemId)
                    .count(adjustment)
                    .build();
        }
        repository.save(item);

        return generateResponse(item, oldCount);
    }

    private InventoryAdjustmentResponse generateResponse(InventoryItem item, int oldCount) {
        return InventoryAdjustmentResponse.builder()
                .itemId(item.getItemId())
                .oldCount(oldCount)
                .newCount(item.getCount())
                .build();
    }
}
