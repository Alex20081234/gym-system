package com.epam.gymsystem.steps.integration;

import com.epam.gymsystem.cucumber.CucumberSpringConfiguration;
import com.epam.gymsystem.domain.Trainee;
import com.epam.gymsystem.domain.Trainer;
import com.epam.gymsystem.domain.Training;
import com.epam.gymsystem.domain.TrainingType;
import com.epam.gymsystem.security.JwtService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
public class ControllersIntegrationStepDefinitions extends CucumberSpringConfiguration {
    @Autowired
    private EntityManagerFactory factory;
    private EntityManager entityManager;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MongoTemplate template;
    private String jwt;
    private ResponseEntity<?> response;

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

    @When("I send a DELETE request to {string}")
    public void iSendADELETERequestTo(String endpoint) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        response = testRestTemplate.exchange(endpoint, HttpMethod.DELETE, requestEntity, Void.class);
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int statusCode) {
        assertEquals(statusCode, response.getStatusCode().value());
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
    }

    @And("a valid JWT")
    public void aValidJWT() {
        User.UserBuilder builder;
        builder = User.withUsername("John.Doe");
        builder.password("password");
        builder.roles("USER");
        jwt = jwtService.generateJwtToken(new UsernamePasswordAuthenticationToken(builder.build(), "password"));
    }

    @And("an existing trainer workload info")
    public void anExistingTrainerWorkloadInfo() {
        MonthlyWorkload m = MonthlyWorkload.builder()
                .month(Month.JANUARY)
                .workingHours(120)
                .build();
        YearlyWorkload y = YearlyWorkload.builder()
                .year(2025)
                .list(List.of(m))
                .build();
        TrainerSummary summary = TrainerSummary.builder()
                .username("Jane.Smith")
                .firstName("Jane")
                .lastName("Smith")
                .status(true)
                .workloads(List.of(y))
                .build();
        template.save(summary);
    }

    @And("db shouldn't have trainee info")
    public void dbShouldnTHaveTraineeInfo() {
        entityManager = factory.createEntityManager();
        Optional<Trainee> trainee;
        try {
            trainee = Optional.ofNullable(entityManager.createQuery("from Trainee where username = :username", Trainee.class)
                    .setParameter("username", "John.Doe")
                    .getSingleResult());
        } catch (NoResultException e) {
            trainee = Optional.empty();
        }
        entityManager.close();
        assertNull(trainee.orElse(null));
    }

    @And("trainer's workload should decrease")
    public void trainerSWorkloadShouldDecrease() {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is("Jane.Smith"));
        TrainerSummary summary = await().atMost(5, TimeUnit.SECONDS).until(() -> template.findOne(query, TrainerSummary.class), Objects::nonNull);
        assertEquals(60, summary.getWorkloads().get(0).getList().get(0).getWorkingHours());
    }

    @When("I send a POST request to {string} with {string}")
    public void iSendAPOSTRequestTo(String endpoint, String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        response = testRestTemplate.exchange(endpoint, HttpMethod.POST, requestEntity, Void.class);
    }

    @And("db should have new training info")
    public void dbShouldHaveNewTrainingInfo() {
        entityManager = factory.createEntityManager();
        Optional<Training> training = Optional.ofNullable(entityManager.createQuery("from Training where trainee.username = :username", Training.class)
                .setParameter("username", "John.Doe")
                .getSingleResult());
        entityManager.close();
        assertNotNull(training.orElse(null));
    }

    @And("trainer's workload should increase")
    public void trainerSWorkloadShouldIncrease() {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is("Jane.Smith"));
        TrainerSummary summary = await().atMost(5, TimeUnit.SECONDS).until(() -> template.findOne(query, TrainerSummary.class), Objects::nonNull);
        assertEquals(60, summary.getWorkloads().get(0).getList().get(0).getWorkingHours());
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MonthlyWorkload {
        private Month month;
        private int workingHours;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class YearlyWorkload {
        private int year;
        private List<MonthlyWorkload> list;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Document(collection = "TrainerSummaries")
    private static class TrainerSummary {
        @Id
        private String id;
        @Field(name = "username")
        @Indexed(unique = true)
        private String username;
        @Field(name = "firstName")
        private String firstName;
        @Field(name = "lastName")
        private String lastName;
        @Field(name = "status")
        private boolean status;
        @Field(name = "workload")
        private List<YearlyWorkload> workloads;
    }
}
