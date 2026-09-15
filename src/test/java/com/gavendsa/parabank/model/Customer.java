package com.gavendsa.parabank.model;

public record Customer(
        String firstName, String lastName, String street, String city,
        String state, String zipCode, String phoneNumber, String ssn,
        String username, String password) {

    // ParaBank's username column is VARCHAR(20); a longer value is rejected as
    // "already exists" rather than a length error. See DEF-001, found in the
    // companion Playwright suite against this same application.
    public static Customer generate() {
        String unique = (System.currentTimeMillis() % 1_000_000) + "" + (int) (Math.random() * 1000);
        return new Customer("Test", "Customer", "123 Avenue", "Montreal", "Quebec",
                "H4A3L5", "1234567890", "987654321", "u_" + unique, "Test1234");
    }
}