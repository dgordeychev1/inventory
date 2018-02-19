package com.inventory.controllers;

import com.inventory.controllers.dto.InventoryAdjustmentResponse;
import com.inventory.controllers.dto.InventoryRequest;
import com.inventory.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @RequestMapping(value = "/message_queue", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public InventoryAdjustmentResponse updateInventory(@RequestBody InventoryRequest inventoryRequest) {
        return inventoryService.updateInventory(inventoryRequest);
    }

    @RequestMapping(value = "{itemId}/count", method = RequestMethod.GET)
    public Integer getItemCount(@PathVariable("itemId") String itemId) {
        return inventoryService.getItemCount(itemId);
    }
}
