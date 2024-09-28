package com.epam.task.gymsystem.configuration;

import com.epam.task.gymsystem.domain.*;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Random;

@Configuration
public class GymSystemConfiguration {
    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public SessionFactory sessionFactory() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.configure();
        configuration.addAnnotatedClass(Trainee.class);
        configuration.addAnnotatedClass(Trainer.class);
        configuration.addAnnotatedClass(Training.class);
        configuration.addAnnotatedClass(TrainingType.class);
        configuration.addAnnotatedClass(User.class);
        return configuration.buildSessionFactory();
    }
}
