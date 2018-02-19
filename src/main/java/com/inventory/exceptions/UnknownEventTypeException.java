package com.inventory.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Unknown Event Type")
public class UnknownEventTypeException extends RuntimeException {
    public UnknownEventTypeException() {}
}
