package com.example.carrental.exceptions;

import lombok.Data;

@Data
public class CarNotFoundException extends RuntimeException {

    public CarNotFoundException(String message) {
        super(message);
    }

}
