package com.epam.gymsystem.steps.component;

import com.epam.gymsystem.cucumber.CucumberSpringConfiguration;
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@CucumberContextConfiguration
public class ControllersComponentStepDefinitions extends CucumberSpringConfiguration {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private EntityManagerFactory factory;
    private EntityManager entityManager;
    @MockBean
    private MessageSenderService messageSenderService;
    private String jwt;
    private ResponseEntity<?> response;

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int statusCode) {
        assertEquals(statusCode, response.getStatusCode().value());
    }

    @And("the response body should contain valid JWT")
    public void theResponseBodyShouldContainValidJWT() {
        assertTrue(jwtService.validateJwtToken(response.getBody().toString()));
    }

    @Given("a valid {string} JWT")
    public void aValidJWT(String user) {
        User.UserBuilder builder;
        if (user.equals("trainee")) {
            builder = User.withUsername("John.Doe");
        } else {
            builder = User.withUsername("Jane.Smith");
        }
        builder.password("password");
        builder.roles("USER");
        jwt = jwtService.generateJwtToken(new UsernamePasswordAuthenticationToken(builder.build(), "password"));
    }

    @And("the response body should contain {string}")
    public void theResponseBodyShouldContain(String exceptionMessage) {
        assertTrue(response.getBody().toString().contains(exceptionMessage));
    }

    @Given("nonexistent credentials")
    public void nonexistentCredentials() {}

    @Given("an invalid JWT")
    public void anInvalidJWT() {
        jwt = "invalid";
    }

    @When("I send a login GET request to {string}")
    public void iSendALoginGETRequestTo(String endpoint) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        response = testRestTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, String.class);
    }

    @When("I send a logout GET request to {string} {string} exception")
    public void iSendALogoutGETRequestTo(String endpoint, String isExpException) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        if (isExpException.contains("expecting")) {
            response = testRestTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, String.class);
        } else {
            response = testRestTemplate.exchange(endpoint, HttpMethod.GET, requestEntity, Void.class);
        }
    }

    @And("the response body should contain valid credentials")
    public void theResponseBodyShouldContainValidCredentials() {
        Credentials credentials = (Credentials) response.getBody();
        assertTrue(credentials.getUsername().matches("^[A-Z][a-z]*\\.[A-Z][a-z]*\\d*$"));
        assertEquals(10, credentials.getPassword().length());
    }

    @When("I send a PUT request to {string} with {string} in body expecting {string}")
    public void iSendAPUTRequestToWithOldPasswordPasswordNewPasswordNewPasswordInBody(String endpoint, String body, String responseType) {
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
                .password(encoder.encode("password"))
                .username("John.Doe")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .isActive(true)
                .build();
        entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(trainee);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @When("I send a GET request to {string} expecting {string}")
    public void iSendAGETRequestTo(String endpoint, String responseType) {
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

    @When("I send a DELETE request to {string}")
    public void iSendADELETERequestTo(String endpoint) {
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
                .password(encoder.encode("password"))
                .isActive(true)
                .specialization(TrainingType.builder().name("Yoga").id(1).build())
                .trainings(null)
                .build();
        entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(trainer);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Given("an existing trainee trainers info")
    public void anExistingTraineeTrainersInfo() {
        Trainer trainer = Trainer.builder()
                .firstName("Jane")
                .lastName("Smith")
                .username("Jane.Smith")
                .password(encoder.encode("password"))
                .isActive(true)
                .specialization(TrainingType.builder().name("Yoga").id(1).build())
                .build();
        Trainee trainee = Trainee.builder()
                .password(encoder.encode("password"))
                .username("John.Doe")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .isActive(true)
                .build();
        entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(trainee);
        entityManager.persist(trainer);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @When("I send a PATCH request to {string} expecting {string}")
    public void iSendAPATCHRequestTo(String endpoint, String responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        if (responseType.equals("nothing")) {
            response = testRestTemplate.exchange(endpoint, HttpMethod.PATCH, requestEntity, Void.class);
        } else {
            response = testRestTemplate.exchange(endpoint, HttpMethod.PATCH, requestEntity, String.class);
        }
    }

    @When("I send a POST request to {string} with {string} in body expecting {string}")
    public void iSendAPOSTRequestToWithFirstNameJohnLastNameDoeDateOfBirthAddressMainStInBody(String endpoint, String body, String responseType) {
        HttpHeaders headers = new HttpHeaders();
        if (jwt != null) {
            headers.setBearerAuth(jwt);
        }
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

    @Given("an existing trainer info")
    public void anExistingTrainerInfo() {
        Trainer trainer = Trainer.builder()
                .password(encoder.encode("password"))
                .firstName("Jane")
                .lastName("Smith")
                .username("Jane.Smith")
                .isActive(true)
                .specialization(TrainingType.builder().name("Yoga").id(1).build())
                .build();
        entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(trainer);
        entityManager.getTransaction().commit();
        entityManager.close();
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

    @Given("an existing training creation info")
    public void anExistingTrainingCreationInfo() {
        Trainee trainee = Trainee.builder()
                .username("John.Doe")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .password(encoder.encode("password"))
                .isActive(true)
                .build();
        Trainer trainer = Trainer.builder()
                .firstName("Jane")
                .lastName("Smith")
                .username("Jane.Smith")
                .password(encoder.encode("password"))
                .isActive(true)
                .specialization(TrainingType.builder().name("Yoga").id(1).build())
                .build();
        entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(trainee);
        entityManager.persist(trainer);
        entityManager.getTransaction().commit();
        entityManager.close();
        doNothing().when(messageSenderService).sendMessage(any());
    }

    @And("the response body should contain List of ShortTrainingType with correct fields")
    public void theResponseBodyShouldContainListOfShortTrainingTypeWithCorrectFields() {
        List<ShortTrainingType> list = (List<ShortTrainingType>) response.getBody();
        assertEquals(10, list.size());
        ShortTrainingType type = list.get(0);
        assertEquals(1, type.getId());
        assertEquals("Yoga", type.getName());
    }

    @Given("an existing training info")
    public void anExistingTrainingInfo() {
        Trainee trainee = Trainee.builder()
                .password(encoder.encode("password"))
                .username("John.Doe")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .isActive(true)
                .build();
        Trainer trainer = Trainer.builder()
                .password(encoder.encode("password"))
                .firstName("Jane")
                .lastName("Smith")
                .username("Jane.Smith")
                .isActive(true)
                .specialization(TrainingType.builder().name("Yoga").id(1).build())
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
        entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(trainee);
        entityManager.persist(trainer);
        entityManager.persist(training);
        entityManager.getTransaction().commit();
        entityManager.close();
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
}
