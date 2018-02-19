package com.inventory.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Item Not Found")
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException() {}
}
