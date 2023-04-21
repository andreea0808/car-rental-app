package com.example.carrental.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@RequiredArgsConstructor
public class ErrorDetails {
    private final Date timestamp;
    private final String message;
    private final HttpStatus httpStatus;
}