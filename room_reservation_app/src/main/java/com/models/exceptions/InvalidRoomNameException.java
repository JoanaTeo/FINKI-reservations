package com.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidRoomNameException extends RuntimeException {

    public InvalidRoomNameException() {
        super(InvalidRoomNameException.class.getSimpleName());
    }
}
