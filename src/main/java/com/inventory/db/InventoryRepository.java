package com.inventory.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
	@Query(value = "UPDATE inventory SET item_count = item_count + (?1) WHERE item_id = (?2)", nativeQuery = true)
	void adjustItemCount(Integer count, String itemId);

	@Query(value = "UPDATE inventory SET item_count = (?1) WHERE item_id = (?2)", nativeQuery = true)
	void updateItemCount(Integer count, String itemId);

	@Query(value = "SELECT item_count FROM inventory WHERE item_id = (?1)", nativeQuery = true)
	Optional<Integer> getItemCount(String itemId);

	@Query(value = "SELECT * FROM inventory WHERE item_id = (?1)", nativeQuery = true)
	Optional<InventoryItem> getItemById(String itemId);
}
