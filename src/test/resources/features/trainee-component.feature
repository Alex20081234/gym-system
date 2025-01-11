@component
Feature: Trainee Controller API Tests

  # Positive Test Cases
  Scenario: Register trainee with valid request
    When I send a POST request to "/api/v1/trainees" with "{\"firstName\":\"John\",\"lastName\": \"Doe\", \"dateOfBirth\": \"1990-01-01\", \"address\": \"123 Main St\"}" in body expecting "Credentials"
    Then the response status should be 201
    And the response body should contain valid credentials

  Scenario: Change trainee's login with existing info
    Given an existing trainee info
    And a valid "trainee" JWT
    When I send a PUT request to "/api/v1/trainees/login/John.Doe" with "{\"oldPassword\":\"password\",\"newPassword\":\"newPassword\"}" in body expecting "nothing"
    Then the response status should be 204
    
  Scenario: Get trainee with existing info
    Given an existing trainee info
    And a valid "trainee" JWT
    When I send a GET request to "/api/v1/trainees/John.Doe" expecting "ResponseTrainee"
    Then the response status should be 200
    And the response body should contain ResponseTrainee with correct fields

  Scenario: Update trainee's profile with existing info
    Given an existing trainee info
    And a valid "trainee" JWT
    When I send a PUT request to "/api/v1/trainees/profile/John.Doe" with "{\"address\":\"123 Side St\"}" in body expecting "ResponseTraineeWithUsername"
    Then the response status should be 200
    And the response body should contain ResponseTraineeWithUsername with correct fields

  Scenario: Delete trainee's info with existing info
    Given an existing trainee info
    And a valid "trainee" JWT
    When I send a DELETE request to "/api/v1/trainees/John.Doe"
    Then the response status should be 204
    
  Scenario: Get not assigned trainee's trainers with existing info
    Given an existing trainee not assigned trainers info
    And an existing trainee info
    And a valid "trainee" JWT
    When I send a GET request to "/api/v1/trainees/not-assigned-trainers/John.Doe" expecting "List of ShortTrainer"
    Then the response status should be 200
    And the response body should contain List of ShortTrainer with correct fields

  Scenario: Update trainee's trainers with existing info
    Given an existing trainee trainers info
    And a valid "trainee" JWT
    When I send a PUT request to "/api/v1/trainees/John.Doe/trainers" with "[{\"username\":\"Jane.Smith\",\"required\":true}]" in body expecting "List of ShortTrainer"
    Then the response status should be 200
    And the response body should contain List of ShortTrainer with correct fields
    
  Scenario: Change activity status with existing info
    Given an existing trainee info
    And a valid "trainee" JWT
    When I send a PATCH request to "/api/v1/trainees/activity-status/John.Doe?isActive=false" expecting "nothing"
    Then the response status should be 204
    
  # Negative Test Cases
  Scenario: Change trainee's login with invalid password
    Given an existing trainee info
    And a valid "trainee" JWT
    When I send a PUT request to "/api/v1/trainees/login/John.Doe" with "{\"oldPassword\":\"invalid\",\"newPassword\":\"newPassword\"}" in body expecting "String"
    Then the response status should be 400
    And the response body should contain "Incorrect password"

  Scenario: Change activity status with nonexistent info
    Given an existing trainee info
    And a valid "trainee" JWT
    When I send a PATCH request to "/api/v1/trainees/activity-status/Nonexistent?isActive=false" expecting "exception"
    Then the response status should be 404
    And the response body should contain "Trainee with username Nonexistent was not found"

  Scenario: Change activity status with invalid JWT
    Given an invalid JWT
    When I send a PATCH request to "/api/v1/trainees/activity-status/John.Doe?isActive=false" expecting "exception"
    Then the response status should be 401
