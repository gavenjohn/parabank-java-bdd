package com.gavendsa.parabank.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/parabank/index.htm");
    }

    public void login(String username, String password) {
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("input[type=submit]")).click();

        // Selenium does not auto-wait for navigation the way Playwright does -
        // click() can return before the new page's title has actually updated.
        // Wait for the real target state instead of checking once immediately.
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.titleIs("ParaBank | Accounts Overview"));
    }

    public boolean isOnAccountsOverview() {
        return driver.getTitle().equals("ParaBank | Accounts Overview");
    }
}