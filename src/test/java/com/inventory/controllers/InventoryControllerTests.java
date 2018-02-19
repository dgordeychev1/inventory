package com.inventory.controllers;

import com.inventory.controllers.dto.InventoryAdjustmentResponse;
import com.inventory.controllers.dto.InventoryRequest;
import com.inventory.services.InventoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InventoryControllerTests {

    private MockMvc mvc;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    @Before
    public void setup() throws IOException {

        InventoryRequest adjustmentRequest = InventoryRequest.builder()
                .type("inventory_adjustment")
                .itemId("item001")
                .adjustment(-5)
                .build();
        InventoryRequest updateRequest = InventoryRequest.builder()
                .type("full_sync")
                .itemId("item001")
                .adjustment(200)
                .build();

        when(inventoryService.updateInventory(adjustmentRequest))
                .thenReturn(InventoryAdjustmentResponse.builder()
                        .itemId("item001")
                        .oldCount(100)
                        .newCount(95)
                        .build());
        when(inventoryService.updateInventory(updateRequest))
                .thenReturn(InventoryAdjustmentResponse.builder()
                        .itemId("item001")
                        .oldCount(100)
                        .newCount(200)
                        .build());
        when(inventoryService.getItemCount("item001"))
                .thenReturn(100);

        this.mvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
    }

    @Test
    public void adjustInventory() throws Exception {
        mvc
                .perform(
                        post("/message_queue")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(Files.readAllBytes(Paths.get("src/test/resources/__files/AdjustRequest.json"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId").value("item001"))
                .andExpect(jsonPath("$.oldCount").value(100))
                .andExpect(jsonPath("$.newCount").value(95));
    }

    @Test
    public void updateInventory() throws Exception {
        mvc
                .perform(
                        post("/message_queue")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(Files.readAllBytes(Paths.get("src/test/resources/__files/UpdateRequest.json"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId").value("item001"))
                .andExpect(jsonPath("$.oldCount").value(100))
                .andExpect(jsonPath("$.newCount").value(200));
    }

    @Test
    public void getItemCount() throws Exception {

        mvc
                .perform(
                        get("/item001/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(100));
    }
}
