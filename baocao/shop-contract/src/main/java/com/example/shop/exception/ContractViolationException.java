package com.example.shop.exception;

public class ContractViolationException extends RuntimeException {
    public ContractViolationException(String message) {
        super(message);
    }
}