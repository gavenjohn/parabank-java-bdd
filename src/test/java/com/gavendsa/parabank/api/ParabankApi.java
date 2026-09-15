package com.gavendsa.parabank.api;

import com.gavendsa.parabank.model.Customer;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public final class ParabankApi {

    private ParabankApi() {
    }

    private static String baseUrl() {
        return System.getProperty("baseUrl", "http://localhost:8080");
    }

    public static Customer registerNewCustomer() {
        RestAssured.baseURI = baseUrl();
        Customer customer = Customer.generate();

        // Unlike Playwright's request context, RestAssured's given() does not
        // share cookies between calls automatically - the session established by
        // this GET has to be carried into the POST explicitly.
        Response getResponse = given().get("/parabank/register.htm");
        String sessionId = getResponse.getCookie("JSESSIONID");

        Response response = given()
                .cookie("JSESSIONID", sessionId)
                .formParam("customer.firstName", customer.firstName())
                .formParam("customer.lastName", customer.lastName())
                .formParam("customer.address.street", customer.street())
                .formParam("customer.address.city", customer.city())
                .formParam("customer.address.state", customer.state())
                .formParam("customer.address.zipCode", customer.zipCode())
                .formParam("customer.phoneNumber", customer.phoneNumber())
                .formParam("customer.ssn", customer.ssn())
                .formParam("customer.username", customer.username())
                .formParam("customer.password", customer.password())
                .formParam("repeatedPassword", customer.password())
                .post("/parabank/register.htm");

        if (!response.getBody().asString().contains("Your account was created successfully")) {
            throw new IllegalStateException("Registration rejected for " + customer.username()
                    + ": " + response.getBody().asString());
        }

        return customer;
    }

    public static Response getAccount(int accountId) {
        RestAssured.baseURI = baseUrl();
        return given()
                .header("Accept", "application/json")
                .get("/parabank/services/bank/accounts/" + accountId);
    }
}