package com.epam.gymsystem.steps;

import com.epam.gymsystem.common.UserNotFoundException;
import com.epam.gymsystem.cucumber.CucumberSpringConfiguration;
import com.epam.gymsystem.dao.UserTriesDao;
import com.epam.gymsystem.domain.Trainee;
import com.epam.gymsystem.domain.Trainer;
import com.epam.gymsystem.domain.Training;
import com.epam.gymsystem.domain.TrainingType;
import com.epam.gymsystem.dto.*;
import com.epam.gymsystem.security.JwtService;
import com.epam.gymsystem.service.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@CucumberContextConfiguration
@ExtendWith(MockitoExtension.class)
public class ControllersComponentStepDefinitions extends CucumberSpringConfiguration {
    @MockBean
    private UserTriesDao dao;
    @MockBean
    private AuthService authService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @SpyBean
    private JwtService jwtService;
    @SpyBean
    private PasswordEncoder encoder;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private TraineeService traineeService;
    @MockBean
    private TrainerService trainerService;
    @MockBean
    private TrainingService trainingService;
    @MockBean
    private MessageSenderService messageSenderService;
    private String jwt;
    private ResponseEntity<?> response;

    @Given("valid credentials")
    public void validCredentials() {
        when(dao.isBlocked(anyString())).thenReturn(false);
        when(authService.selectPassword(anyString())).thenReturn("password");
        when(encoder.matches(anyString(), anyString())).thenReturn(true);
        User.UserBuilder builder = User.withUsername("John.Doe");
        builder.password("password");
        builder.roles("USER");
        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(builder.build(), "password"));
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int statusCode) {
        assertEquals(statusCode, response.getStatusCode().value());
    }

    @And("the response body should contain valid JWT")
    public void theResponseBodyShouldContainValidJWT() {
        assertTrue(jwtService.validateJwtToken(response.getBody().toString()));
    }

    @Given("a valid JWT")
    public void aValidJWT() {
        User.UserBuilder builder = User.withUsername("John.Doe");
        builder.password("password");
        builder.roles("USER");
        jwt = jwtService.generateJwtToken(new UsernamePasswordAuthenticationToken(builder.build(), "password"));
    }

    @Given("invalid credentials")
    public void invalidCredentials() {
        when(dao.isBlocked(anyString())).thenReturn(false);
        when(authService.selectPassword(anyString())).thenReturn("different");
        when(encoder.matches(anyString(), anyString())).thenReturn(false);
        doNothing().when(dao).incrementAttemptsOrCreateEntry(anyString());
        when(dao.attempts(anyString())).thenReturn(1);
    }

    @And("the response body should contain {string}")
    public void theResponseBodyShouldContain(String exceptionMessage) {
        assertTrue(response.getBody().toString().contains(exceptionMessage));
    }

    @Given("nonexistent credentials")
    public void nonexistentCredentials() {
        doThrow(new UserNotFoundException("User with username Nonexistent was not found")).when(authService).selectPassword(anyString());
    }

    @Given("an invalid JWT")
    public void anInvalidJWT() {
        jwt = "invalid";
    }

    @And("the service throws an unexpected error")
    public void theServiceThrowsAnUnexpectedError() {
        doThrow(new RuntimeException("Unexpected database error")).when(jwtService).blacklistToken(anyString());
    }

    @When("I send a login GET request to {string}")
    public void iSendALoginGETRequestTo(String endpoint) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        response = testRestTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, String.class);
    }

    @When("I send a logout GET request to {string} {string} exception")
    public void iSendALogoutGETRequestTo(String endpoint, String isExpException) {
        if (!jwt.equals("invalid")) {
            setUpUserDetails();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        if (isExpException.contains("not")) {
            response = testRestTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, Void.class);
        } else if (isExpException.contains("4xx")) {
            await().atMost(1, TimeUnit.SECONDS).until(() -> {
                response = testRestTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, String.class);
                return response.getStatusCode().is4xxClientError();
            });
        } else {
            await().atMost(1, TimeUnit.SECONDS).until(() -> {
                response = testRestTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, String.class);
                return response.getStatusCode().is5xxServerError();
            });
        }
    }

    @And("the response body should contain valid credentials")
    public void theResponseBodyShouldContainValidCredentials() {
        Credentials credentials = (Credentials) response.getBody();
        assertTrue(credentials.getUsername().matches("^[A-Z][a-z]*\\.[A-Z][a-z]*\\d*$"));
        assertEquals(10, credentials.getPassword().length());
    }

    @Given("an existing trainee login info")
    public void anExistingLoginInfo() {
        when(authService.selectPassword(anyString())).thenReturn("password");
        doNothing().when(traineeService).changePassword(anyString(), anyString());
    }

    @When("I send a PUT request to {string} with {string} in body expecting {string}")
    public void iSendAPUTRequestToWithOldPasswordPasswordNewPasswordNewPasswordInBody(String endpoint, String body, String responseType) {
        setUpUserDetails();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        switch(responseType) {
            case "nothing":
                response = testRestTemplate.exchange(endpoint, HttpMethod.PUT, requestEntity, Void.class);
                break;
            case "ResponseTraineeWithUsername":
                response = testRestTemplate.exchange(endpoint, HttpMethod.PUT, requestEntity, ResponseTraineeWithUsername.class);
                break;
            case "ResponseTrainerWithUsername":
                response = testRestTemplate.exchange(endpoint, HttpMethod.PUT, requestEntity, ResponseTrainerWithUsername.class);
                break;
            case "List of ShortTrainer":
                response = testRestTemplate.exchange(endpoint, HttpMethod.PUT, requestEntity, new ParameterizedTypeReference<List<ShortTrainer>>() {});
                break;
            default:
                response = testRestTemplate.exchange(endpoint, HttpMethod.PUT, requestEntity, String.class);
        }
    }

    @Given("an existing trainee info")
    public void anExistingTraineeInfo() {
        Trainee trainee = Trainee.builder()
                .username("John.Doe")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .isActive(true)
                .trainings(null)
                .build();
        when(traineeService.select(anyString())).thenReturn(Optional.of(trainee));
    }

    @When("I send a GET request to {string} expecting {string}")
    public void iSendAGETRequestTo(String endpoint, String responseType) {
        setUpUserDetails();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        switch(responseType) {
            case "ResponseTrainee":
                response = testRestTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, ResponseTrainee.class);
                break;
            case "ResponseTrainer":
                response = testRestTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, ResponseTrainer.class);
                break;
            case "List of ShortTrainer":
                response = testRestTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<ShortTrainer>>() {});
                break;
            case "List of ShortTrainingType":
                response = testRestTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<ShortTrainingType>>() {});
                break;
            case "List of ResponseTraining":
                response = testRestTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<ResponseTraining>>() {});
                break;
            default:
                response = testRestTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, String.class);
        }
    }

    @Given("an existing trainee update info")
    public void anExistingTraineeUpdateInfo() {
        when(traineeService.update(anyString(), any())).thenReturn("John.Doe");
        Trainee trainee = Trainee.builder()
                .username("John.Doe")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Side St")
                .isActive(true)
                .trainings(null)
                .build();
        when(traineeService.select(anyString())).thenReturn(Optional.of(trainee));
    }

    @Given("an existing trainee delete info")
    public void anExistingTraineeDeleteInfo() {
        when(trainingService.selectTrainings(anyString(), any())).thenReturn(new ArrayList<>());
        doNothing().when(traineeService).delete(anyString());
    }

    @When("I send a DELETE request to {string}")
    public void iSendADELETERequestTo(String endpoint) {
        setUpUserDetails();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        response = testRestTemplate.exchange(endpoint, HttpMethod.DELETE, requestEntity, Void.class);
    }

    @Given("an existing trainee not assigned trainers info")
    public void anExistingTraineeNotAssignedTrainersInfo() {
        Trainer trainer = Trainer.builder()
                .firstName("Jane")
                .lastName("Smith")
                .username("Jane.Smith")
                .isActive(true)
                .specialization(TrainingType.builder().name("Yoga").id(1).build())
                .trainings(null)
                .build();
        when(traineeService.selectNotAssignedTrainers(anyString())).thenReturn(List.of(trainer));
    }

    @Given("an existing trainee trainers info")
    public void anExistingTraineeTrainersInfo() {
        doNothing().when(traineeService).updateTrainers(anyString(), any());
        Trainer trainer = Trainer.builder()
                .firstName("Jane")
                .lastName("Smith")
                .username("Jane.Smith")
                .isActive(true)
                .specialization(TrainingType.builder().name("Yoga").id(1).build())
                .trainings(null)
                .build();
        Trainee trainee = Trainee.builder()
                .username("John.Doe")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .isActive(true)
                .trainings(null)
                .trainers(Set.of(trainer))
                .build();
        when(traineeService.select(anyString())).thenReturn(Optional.of(trainee));
        doNothing().when(traineeService).loadDependencies(any());
    }

    @Given("an existing trainee change activity status info")
    public void anExistingTraineeChangeActivityStatusInfo() {
        doNothing().when(traineeService).changeActivityStatus(anyString(), anyBoolean());
    }

    @When("I send a PATCH request to {string} expecting {string}")
    public void iSendAPATCHRequestTo(String endpoint, String responseType) {
        setUpUserDetails();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        if (responseType.equals("nothing")) {
            response = testRestTemplate.exchange(endpoint, HttpMethod.PATCH, requestEntity, Void.class);
        } else {
            response = testRestTemplate.exchange(endpoint, HttpMethod.PATCH, requestEntity, String.class);
        }
    }

    @Given("a nonexistent trainee")
    public void aNonexistentTrainee() {
        doThrow(new UserNotFoundException("Trainee with username Nonexistent was not found")).when(traineeService).changeActivityStatus(anyString(), anyBoolean());
    }

    @And("change trainee's activity status method throws an unexpected error")
    public void changeActivityStatusMethodThrowsAnUnexpectedError() {
        doThrow(new RuntimeException("Unexpected database error")).when(traineeService).changeActivityStatus(anyString(), anyBoolean());
    }

    @When("I send a POST request to {string} with {string} in body expecting {string}")
    public void iSendAPOSTRequestToWithFirstNameJohnLastNameDoeDateOfBirthAddressMainStInBody(String endpoint, String body, String responseType) {
        setUpUserDetails();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        switch(responseType) {
            case "Credentials":
                response = testRestTemplate.exchange(endpoint, HttpMethod.POST, requestEntity, Credentials.class);
                break;
            case "nothing":
                response = testRestTemplate.exchange(endpoint, HttpMethod.POST, requestEntity, Void.class);
                break;
            default:
                response = testRestTemplate.exchange(endpoint, HttpMethod.POST, requestEntity, String.class);
        }
    }

    @And("available create trainee service")
    public void availableCreateService() {
        when(traineeService.create(any())).thenReturn(Credentials.builder().username("John.Doe").password("password12").build());
    }

    @And("the response body should contain ResponseTrainee with correct fields")
    public void theResponseBodyShouldContainResponseTraineeWithCorrectFields() {
        ResponseTrainee trainee = (ResponseTrainee) response.getBody();
        assertEquals("John", trainee.getFirstName());
        assertEquals("Doe", trainee.getLastName());
        assertEquals("1990-01-01", trainee.getDateOfBirth());
        assertEquals("123 Main St", trainee.getAddress());
        assertTrue(trainee.getIsActive());
        assertEquals(0, trainee.getTrainers().size());
    }

    @And("the response body should contain ResponseTraineeWithUsername with correct fields")
    public void theResponseBodyShouldContainResponseTraineeWithUsernameWithCorrectFields() {
        ResponseTraineeWithUsername trainee = (ResponseTraineeWithUsername) response.getBody();
        assertEquals("John", trainee.getFirstName());
        assertEquals("Doe", trainee.getLastName());
        assertEquals("1990-01-01", trainee.getDateOfBirth());
        assertEquals("123 Side St", trainee.getAddress());
        assertTrue(trainee.getIsActive());
        assertEquals(0, trainee.getTrainers().size());
        assertEquals("John.Doe", trainee.getUsername());
    }

    @And("the response body should contain List of ShortTrainer with correct fields")
    public void theResponseBodyShouldContainListOfShortTrainerWithCorrectFields() {
        List<ShortTrainer> list = (List<ShortTrainer>) response.getBody();
        ShortTrainer trainer = list.get(0);
        assertEquals("Jane.Smith", trainer.getUsername());
        assertEquals("Jane", trainer.getFirstName());
        assertEquals("Smith", trainer.getLastName());
        assertEquals("Yoga", trainer.getSpecialization().getName());
        assertEquals(1, trainer.getSpecialization().getId());
    }

    @And("available create trainer service")
    public void availableCreateTrainerService() {
        when(trainerService.create(any())).thenReturn(Credentials.builder().username("Jane.Smith").password("password12").build());
    }

    @Given("an existing trainer login info")
    public void anExistingTrainerLoginInfo() {
        when(authService.selectPassword(anyString())).thenReturn("password");
        doNothing().when(trainerService).changePassword(anyString(), anyString());
    }

    @Given("an existing trainer info")
    public void anExistingTrainerInfo() {
        Trainer trainer = Trainer.builder()
                .firstName("Jane")
                .lastName("Smith")
                .username("Jane.Smith")
                .isActive(true)
                .specialization(TrainingType.builder().name("Yoga").id(1).build())
                .trainings(null)
                .build();
        when(trainerService.select(anyString())).thenReturn(Optional.of(trainer));
    }

    @And("the response body should contain ResponseTrainer with correct fields")
    public void theResponseBodyShouldContainResponseTrainerWithCorrectFields() {
        ResponseTrainer trainer = (ResponseTrainer) response.getBody();
        assertEquals("Jane", trainer.getFirstName());
        assertEquals("Smith", trainer.getLastName());
        assertEquals("Yoga", trainer.getSpecialization().getName());
        assertEquals(1, trainer.getSpecialization().getId());
        assertTrue(trainer.getIsActive());
        assertEquals(0, trainer.getTrainees().size());
    }

    @Given("an existing trainer update info")
    public void anExistingTrainerUpdateInfo() {
        when(trainerService.update(anyString(), any())).thenReturn("Alexa.Smith");
        Trainer trainer = Trainer.builder()
                .firstName("Alexa")
                .lastName("Smith")
                .username("Alexa.Smith")
                .isActive(true)
                .specialization(TrainingType.builder().name("Yoga").id(1).build())
                .trainings(null)
                .build();
        when(trainerService.select(anyString())).thenReturn(Optional.of(trainer));
    }

    @And("the response body should contain ResponseTrainerWithUsername with correct fields")
    public void theResponseBodyShouldContainResponseTrainerWithUsernameWithCorrectFields() {
        ResponseTrainerWithUsername trainer = (ResponseTrainerWithUsername) response.getBody();
        assertEquals("Alexa", trainer.getFirstName());
        assertEquals("Smith", trainer.getLastName());
        assertEquals("Yoga", trainer.getSpecialization().getName());
        assertEquals(1, trainer.getSpecialization().getId());
        assertTrue(trainer.getIsActive());
        assertEquals(0, trainer.getTrainees().size());
        assertEquals("Alexa.Smith", trainer.getUsername());
    }

    @Given("an existing trainer change activity status info")
    public void anExistingTrainerChangeActivityStatusInfo() {
        doNothing().when(trainerService).changeActivityStatus(anyString(), anyBoolean());
    }

    @Given("a nonexistent trainer")
    public void aNonexistentTrainer() {
        doThrow(new UserNotFoundException("Trainer with username Nonexistent was not found")).when(trainerService).changeActivityStatus(anyString(), anyBoolean());
    }

    @And("change trainer's activity status method throws an unexpected error")
    public void changeTrainerSActivityStatusMethodThrowsAnUnexpectedError() {
        doThrow(new RuntimeException("Unexpected database error")).when(trainerService).changeActivityStatus(anyString(), anyBoolean());
    }

    @Given("an existing training creation info")
    public void anExistingTrainingCreationInfo() {
        Trainee trainee = Trainee.builder()
                .username("John.Doe")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .isActive(true)
                .trainings(null)
                .build();
        when(traineeService.select(anyString())).thenReturn(Optional.of(trainee));
        Trainer trainer = Trainer.builder()
                .firstName("Jane")
                .lastName("Smith")
                .username("Jane.Smith")
                .isActive(true)
                .specialization(TrainingType.builder().name("Yoga").id(1).build())
                .trainings(null)
                .build();
        when(trainerService.select(anyString())).thenReturn(Optional.of(trainer));
        doNothing().when(trainingService).create(any());
        doNothing().when(messageSenderService).sendMessage(any());
    }

    @Given("an existing training types info")
    public void anExistingTrainingTypesInfo() {
        TrainingType type = TrainingType.builder()
                .id(1)
                .name("Yoga")
                .build();
        when(trainingService.selectAllTypes()).thenReturn(List.of(type));
    }

    @And("the response body should contain List of ShortTrainingType with correct fields")
    public void theResponseBodyShouldContainListOfShortTrainingTypeWithCorrectFields() {
        List<ShortTrainingType> list = (List<ShortTrainingType>) response.getBody();
        ShortTrainingType type = list.get(0);
        assertEquals(1, type.getId());
        assertEquals("Yoga", type.getName());
    }

    @Given("an existing training info")
    public void anExistingTrainingInfo() {
        Trainee trainee = Trainee.builder()
                .username("John.Doe")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .isActive(true)
                .trainings(null)
                .build();
        Trainer trainer = Trainer.builder()
                .firstName("Jane")
                .lastName("Smith")
                .username("Jane.Smith")
                .isActive(true)
                .specialization(TrainingType.builder().name("Yoga").id(1).build())
                .trainings(null)
                .build();
        TrainingType type = TrainingType.builder()
                .id(1)
                .name("Yoga")
                .build();
        Training training = Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingName("Yoga")
                .trainingType(type)
                .trainingDate(LocalDate.of(2025, 1, 10))
                .duration(60)
                .build();
        when(trainingService.selectTrainings(anyString(), any())).thenReturn(List.of(training));
    }

    @And("the response body should contain List of ResponseTraining with correct fields")
    public void theResponseBodyShouldContainListOfResponseTrainingWithCorrectFields() {
        List<ResponseTraining> list = (List<ResponseTraining>) response.getBody();
        ResponseTraining training = list.get(0);
        assertEquals("Yoga", training.getName());
        assertEquals("2025-01-10", training.getDate());
        assertEquals("Yoga", training.getType().getName());
        assertEquals(1, training.getType().getId());
        assertEquals(60, training.getDuration());
        assertEquals("Jane.Smith", training.getPartnerName());
    }

    @Given("a nonexistent user")
    public void aNonexistentUser() {
        doThrow(new UserNotFoundException("Trainee with username Nonexistent was not found")).when(traineeService).select(anyString());
    }

    @And("a training service's method select training types throws an unexpected error")
    public void aTrainingServiceSMethodSelectTrainingTypesThrowsAnUnexpectedError() {
        doThrow(new RuntimeException("Unexpected database error")).when(trainingService).selectAllTypes();
    }

    private void setUpUserDetails() {
        User.UserBuilder builder = User.withUsername("John.Doe");
        builder.password("password");
        builder.roles("USER");
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(builder.build());
    }
}
