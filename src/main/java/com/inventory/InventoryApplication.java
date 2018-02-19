package com.inventory;

import com.inventory.db.InventoryItem;
import com.inventory.db.InventoryRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class InventoryApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(InventoryApplication.class, args);

        InventoryRepository repository = context.getBean(InventoryRepository.class);
        repository.save(InventoryItem.builder()
                .itemId("item001")
                .count(100)
                .build());
   }
}
