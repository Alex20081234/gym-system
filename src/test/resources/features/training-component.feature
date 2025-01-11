@component
Feature: Training Controller API Tests

  # Positive Test Cases
  Scenario: Add training with existing info
    Given an existing training creation info
    And a valid "trainee" JWT
    When I send a POST request to "/api/v1/trainings/John.Doe" with "{\"traineeUsername\":\"John.Doe\",\"trainerUsername\":\"Jane.Smith\",\"name\":\"Yoga\",\"date\":\"2025-01-10\",\"duration\":60}" in body expecting "nothing"
    Then the response status should be 204

  Scenario: Get training types with existing info
    Given a valid "trainee" JWT
    And an existing trainee info
    When I send a GET request to "/api/v1/trainings/types/John.Doe" expecting "List of ShortTrainingType"
    Then the response status should be 200
    And the response body should contain List of ShortTrainingType with correct fields

  Scenario: Get user's trainings with existing info
    Given an existing training info
    And a valid "trainee" JWT
    When I send a GET request to "/api/v1/trainings/John.Doe?partnerName=Jane.Smith" expecting "List of ResponseTraining"
    Then the response status should be 200
    And the response body should contain List of ResponseTraining with correct fields

  # Negative Test Cases
  Scenario: Get training types with invalid JWT
    Given an invalid JWT
    When I send a GET request to "/api/v1/trainings/types/John.Doe" expecting "exception"
    Then the response status should be 401

  Scenario: Add training with nonexistent user
    Given an existing trainee info
    And a valid "trainee" JWT
    When I send a POST request to "/api/v1/trainings/Nonexistent" with "{\"traineeUsername\":\"Nonexistent\",\"trainerUsername\":\"Jane.Smith\",\"name\":\"Yoga\",\"date\":\"2025-01-10\",\"duration\":60}" in body expecting "exception"
    Then the response status should be 404
    And the response body should contain "User with username Nonexistent was not found"
