package com.epam.gymsystem.steps.integration;

import com.epam.gymsystem.domain.Trainee;
import io.cucumber.java.After;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;

public class Hooks {
    @Autowired
    private EntityManagerFactory factory;
    @Autowired
    private MongoTemplate template;

    @After
    public void tearDown() {
        SecurityContextHolder.clearContext();
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        try {
            Trainee trainee = em.createQuery("from Trainee where username = :username", Trainee.class)
                    .setParameter("username", "John.Doe")
                    .getSingleResult();
            em.refresh(trainee);
            em.remove(trainee);
        } catch (Exception ignored) {} finally {
            em.createQuery("DELETE from Trainer t where t.username = :username")
                    .setParameter("username", "Jane.Smith")
                    .executeUpdate();
            em.getTransaction().commit();
            em.close();
            Query query = new Query();
            query.addCriteria(Criteria.where("username").is("Jane.Smith"));
            template.remove(query, "TrainerSummaries");
        }
    }
}
