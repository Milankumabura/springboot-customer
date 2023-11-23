package com.milan.exception;

public class CustomerNotFoundException extends RuntimeException {
    private final Integer customerId;

    public CustomerNotFoundException(Integer customerId) {
        super("Customer with ID " + customerId + " not found.");
        this.customerId = customerId;
    }

    public Integer getCustomerId() {
        return customerId;
    }
}
