package com.inventory.services;

import com.inventory.controllers.dto.InventoryAdjustmentResponse;
import com.inventory.controllers.dto.InventoryRequest;
import com.inventory.db.InventoryItem;
import com.inventory.db.InventoryRepository;
import com.inventory.exceptions.ItemNotFoundException;
import com.inventory.exceptions.UnknownEventTypeException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InventoryServiceTests {

	@Autowired
	InventoryService inventoryService;

	@Autowired
	InventoryRepository repository;

	@Before
	public void setup() {
		repository.save(InventoryItem.builder().itemId("item001").count(100).build());
	}

	@After
	public void close() {
		repository.deleteAll();
	}

	@Test
	public void getInventoryItemCount() {
		assertEquals(100, (int)inventoryService.getItemCount("item001"));
	}

	@Test
	public void adjustInventoryItemCount() {
		InventoryRequest inventoryRequest = InventoryRequest.builder()
				.itemId("item001")
				.type("inventory_adjustment")
				.adjustment(-5)
				.build();
		InventoryAdjustmentResponse response = inventoryService.updateInventory(inventoryRequest);
		assertEquals(100, (int)response.getOldCount());
		assertEquals(95, (int)response.getNewCount());
	}

	@Test
	public void updateInventoryItemCount() {
		InventoryRequest inventoryRequest = InventoryRequest.builder()
				.itemId("item001")
				.type("full_sync")
				.adjustment(200)
				.build();
		InventoryAdjustmentResponse response = inventoryService.updateInventory(inventoryRequest);
		assertEquals(100, (int)response.getOldCount());
		assertEquals(200, (int)response.getNewCount());
	}

	@Test(expected = ItemNotFoundException.class)
	public void getInvalidInventoryItemCount() {
		inventoryService.getItemCount("item002");
	}

	@Test
	public void updateNewInventoryItem() {
		assertEquals(1, repository.findAll().size());
		InventoryRequest inventoryRequest = InventoryRequest.builder()
				.itemId("item002")
				.type("full_sync")
				.adjustment(200)
				.build();
		inventoryService.updateInventory(inventoryRequest);
		assertEquals(2, repository.findAll().size());
	}

	@Test(expected = ItemNotFoundException.class)
	public void adjustInvalidInventoryItem() {
		InventoryRequest inventoryRequest = InventoryRequest.builder()
				.itemId("item002")
				.type("inventory_adjustment")
				.adjustment(5)
				.build();
		inventoryService.updateInventory(inventoryRequest);
	}

	@Test(expected = UnknownEventTypeException.class)
	public void updateInvalidInventoryEvent() {
		InventoryRequest inventoryRequest = InventoryRequest.builder()
				.itemId("item001")
				.type("full_sync_adjust")
				.adjustment(200)
				.build();
		inventoryService.updateInventory(inventoryRequest);
	}
}
