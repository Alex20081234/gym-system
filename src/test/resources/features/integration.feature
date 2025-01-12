@integration
Feature: Integration Tests

  # Positive Test Cases
  Scenario: Trainee Deletion
    Given an existing training info
    And a valid JWT
    And an existing trainer workload info
    When I send a DELETE request to "/api/v1/trainees/John.Doe"
    Then the response status should be 204
    And db shouldn't have trainee info
    And trainer's workload should decrease

  Scenario: Training Creation
    Given an existing training creation info
    And a valid JWT
    When I send a POST request to "/api/v1/trainings/John.Doe" with "{\"traineeUsername\":\"John.Doe\",\"trainerUsername\":\"Jane.Smith\",\"name\":\"Yoga\",\"date\":\"2025-01-10\",\"duration\":60}"
    Then the response status should be 204
    And db should have new training info
    And trainer's workload should increase
