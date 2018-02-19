package com.inventory.controllers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class InventoryAdjustmentResponse {
    @NotNull
    @JsonProperty("itemId")
    private String itemId;

    @NotNull
    @JsonProperty("oldCount")
    private Integer oldCount;

    @NotNull
    @JsonProperty("newCount")
    private Integer newCount;
}
