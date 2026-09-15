Feature: Accounts API

  Scenario: A request for a non-existent account returns a clear error
    When a client requests account "999999"
    Then the response status is 400
    And the response body contains "Could not find account #999999"