@component
Feature: Trainer Controller API Tests

  # Positive Test Cases
  Scenario: Register trainer with valid request
    Given a valid JWT
    And available create trainer service
    When I send a POST request to "/api/v1/trainers" with "{\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"specialization\":{\"name\":\"Yoga\",\"id\":1}}" in body expecting "Credentials"
    Then the response status should be 201
    And the response body should contain valid credentials

  Scenario: Change trainer's login with existing info
    Given an existing trainer login info
    And a valid JWT
    When I send a PUT request to "/api/v1/trainers/login/Jane.Smith" with "{\"oldPassword\":\"password\",\"newPassword\":\"newPassword\"}" in body expecting "nothing"
    Then the response status should be 204

  Scenario: Get trainer with existing info
    Given an existing trainer info
    And a valid JWT
    When I send a GET request to "/api/v1/trainers/Jane.Smith" expecting "ResponseTrainer"
    Then the response status should be 200
    And the response body should contain ResponseTrainer with correct fields

  Scenario: Update trainer's profile with existing info
    Given an existing trainer update info
    And a valid JWT
    When I send a PUT request to "/api/v1/trainers/profile/Jane.Smith" with "{\"firstName\":\"Alexa\"}" in body expecting "ResponseTrainerWithUsername"
    Then the response status should be 200
    And the response body should contain ResponseTrainerWithUsername with correct fields

  Scenario: Change activity status with existing info
    Given an existing trainer change activity status info
    And a valid JWT
    When I send a PATCH request to "/api/v1/trainers/activity-status/Jane.Smith?isActive=false" expecting "nothing"
    Then the response status should be 204

  # Negative Test Cases
  Scenario: Change trainer's login with invalid password
    Given an existing trainer login info
    And a valid JWT
    When I send a PUT request to "/api/v1/trainers/login/Jane.Smith" with "{\"oldPassword\":\"invalid\",\"newPassword\":\"newPassword\"}" in body expecting "String"
    Then the response status should be 400
    And the response body should contain "Incorrect password"

  Scenario: Change activity status with nonexistent info
    Given a nonexistent trainer
    And a valid JWT
    When I send a PATCH request to "/api/v1/trainers/activity-status/Nonexistent?isActive=false" expecting "exception"
    Then the response status should be 404
    And the response body should contain "Trainer with username Nonexistent was not found"

  Scenario: Change activity status with invalid JWT
    Given an invalid JWT
    When I send a PATCH request to "/api/v1/trainers/activity-status/Jane.Smith?isActive=false" expecting "exception"
    Then the response status should be 401

  Scenario: Change activity status with unexpected error
    Given a valid JWT
    And change trainer's activity status method throws an unexpected error
    When I send a PATCH request to "/api/v1/trainers/activity-status/Jane.Smith?isActive=false" expecting "exception"
    Then the response status should be 500
    And the response body should contain "Unexpected error occurred : "
