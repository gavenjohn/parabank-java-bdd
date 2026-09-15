package com.gavendsa.parabank.steps;

import com.gavendsa.parabank.api.ParabankApi;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

public class AccountsApiSteps {

    private Response response;

    @When("a client requests account {string}")
    public void aClientRequestsAccount(String accountId) {
        response = ParabankApi.getAccount(Integer.parseInt(accountId));
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(int statusCode) {
        Assertions.assertEquals(statusCode, response.getStatusCode());
    }

    @And("the response body contains {string}")
    public void theResponseBodyContains(String expectedText) {
        Assertions.assertTrue(response.getBody().asString().contains(expectedText));
    }
}