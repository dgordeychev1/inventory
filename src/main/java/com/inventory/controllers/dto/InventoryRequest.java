package com.inventory.controllers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequest {
    @NotNull
    @JsonProperty("type")
    private String type;

    @NotNull
    @JsonProperty("itemId")
    private String itemId;

    @NotNull
    @JsonProperty("adjustment")
    private Integer adjustment;
}
