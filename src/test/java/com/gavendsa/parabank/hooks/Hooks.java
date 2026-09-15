package com.gavendsa.parabank.hooks;

import com.gavendsa.parabank.api.ParabankRegistrationClient;
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

    // Runs once before any scenario. The image ships with an empty schema, and
    // every page redirects to initializeDB.htm until this runs - confirmed
    // identical behaviour in the companion Playwright suite against the same
    // application.
    @BeforeAll
    public static void initializeDatabase() {
        RestAssured.baseURI = System.getProperty("baseUrl", "http://localhost:8080");
        given().get("/parabank/initializeDB.htm");

        // admin.htm reads the Parameter table and 500s until it exists - a
        // reliable signal that initialisation actually completed, rather than
        // trusting the redirect alone.
        int status = given().get("/parabank/admin.htm").statusCode();
        if (status != 200) {
            throw new IllegalStateException(
                    "Database did not initialise - admin.htm returned " + status);
        }
    }

    @Before
    public void setUp() {
        customer = ParabankRegistrationClient.registerNewCustomer();
        DriverFactory.initDriver();
    }

    @After
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}