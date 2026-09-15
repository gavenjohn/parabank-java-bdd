Feature: Login

  @needsCustomer @ui
  Scenario: A registered customer can log in and reach the accounts overview
    Given a registered customer
    When they log in with valid credentials
    Then they see the accounts overview page