package com.gavendsa.parabank.steps;

import com.gavendsa.parabank.hooks.Hooks;
import com.gavendsa.parabank.ui.DriverFactory;
import com.gavendsa.parabank.ui.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class LoginSteps {

    private final Hooks hooks;
    private LoginPage loginPage;

    public LoginSteps(Hooks hooks) {
        this.hooks = hooks;
    }

    @Given("a registered customer")
    public void aRegisteredCustomer() {
        // Registration happens in @Before - this step is here so the scenario
        // reads as business intent rather than skipping straight to login.
    }

    @When("they log in with valid credentials")
    public void theyLogIn() {
        loginPage = new LoginPage(DriverFactory.getDriver());
        loginPage.open(System.getProperty("baseUrl", "http://localhost:8080"));
        loginPage.login(hooks.getCustomer().username(), hooks.getCustomer().password());
    }

    @Then("they see the accounts overview page")
    public void theySeeAccountsOverview() {
        Assertions.assertTrue(loginPage.isOnAccountsOverview());
    }
}