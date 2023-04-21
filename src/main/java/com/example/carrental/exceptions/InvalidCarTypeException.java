package com.example.carrental.exceptions;

import lombok.Data;

@Data
public class InvalidCarTypeException extends RuntimeException {
    public InvalidCarTypeException(String message) {
        super(message);
    }
}