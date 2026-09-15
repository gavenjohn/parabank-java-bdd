package com.gavendsa.parabank.hooks;

import com.gavendsa.parabank.api.ParabankApi;
import com.gavendsa.parabank.model.Customer;
import com.gavendsa.parabank.ui.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

public class Hooks {

    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    // Runs once before any scenario. The image ships with an empty schema;
    // confirmed identical behaviour in the companion Playwright suite.
    @BeforeAll
    public static void initializeDatabase() {
        RestAssured.baseURI = System.getProperty("baseUrl", "http://localhost:8080");
        given().get("/parabank/initializeDB.htm");

        int status = given().get("/parabank/admin.htm").statusCode();
        if (status != 200) {
            throw new IllegalStateException(
                    "Database did not initialise - admin.htm returned " + status);
        }
    }

    // Tagged rather than unconditional - an API-only scenario has no need for a
    // registered customer or a browser, and shouldn't pay for either.
    @Before("@needsCustomer")
    public void registerCustomer() {
        customer = ParabankApi.registerNewCustomer();
    }

    @Before("@ui")
    public void startBrowser() {
        DriverFactory.initDriver();
    }

    @After("@ui")
    public void stopBrowser() {
        DriverFactory.quitDriver();
    }
}