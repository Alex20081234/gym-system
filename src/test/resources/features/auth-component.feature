@component
Feature: Auth Controller API Tests

  # Positive Test Cases
  Scenario: Login with valid username and password
    Given an existing trainee info
    When I send a login GET request to "/api/v1/login?username=John.Doe&password=password"
    Then the response status should be 200
    And the response body should contain valid JWT

  Scenario: Logout with valid active session
    Given an existing trainee info
    And a valid "trainee" JWT
    When I send a logout GET request to "/api/v1/logout" "not expecting" exception
    Then the response status should be 204

  # Negative Test Cases
  Scenario: Login with invalid password
    Given an existing trainee info
    When I send a login GET request to "/api/v1/login?username=John.Doe&password=invalid"
    Then the response status should be 401
    And the response body should contain "Invalid username or password"

  Scenario: Login with a nonexistent user
    Given nonexistent credentials
    When I send a login GET request to "/api/v1/login?username=Nonexistent&password=password"
    Then the response status should be 404
    And the response body should contain "User with username Nonexistent was not found"

  Scenario: Logout with an invalid JWT
    Given an invalid JWT
    When I send a logout GET request to "/api/v1/logout" "expecting" exception
    Then the response status should be 401
