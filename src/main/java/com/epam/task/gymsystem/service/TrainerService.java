package com.epam.task.gymsystem.service;


import com.epam.task.gymsystem.DAO.TrainerDAO;
import com.epam.task.gymsystem.domain.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerService {

    private final TrainerDAO dao;

    @Autowired
    public TrainerService(TrainerDAO dao) {
        this.dao = dao;
    }

    public void create(Trainer trainer) {
        trainer.setUsername(createUsername(trainer.getFirstName(), trainer.getLastName()));
        dao.create(trainer);
    }

    public void update(Long userId, Trainer changes) {
        Trainer trainer = dao.select(userId);
        boolean nameChanged = false;
        if (changes.getUserId() != null) {
            trainer.setUserId(changes.getUserId());
        }
        if (changes.getSpecialization() != null) {
            trainer.setSpecialization(changes.getSpecialization());
        }
        if (changes.getFirstName() != null) {
            trainer.setFirstName(changes.getFirstName());
            nameChanged = true;
        }
        if (changes.getLastName() != null) {
            trainer.setLastName(changes.getLastName());
            nameChanged = true;
        }
        if (nameChanged) {
            trainer.setUsername(createUsername(trainer.getFirstName(), trainer.getLastName()));
        }
        if (changes.getPassword() != null) {
            trainer.setPassword(changes.getPassword());
        }
        trainer.setActive(changes.isActive());
        dao.update(trainer);
    }

    public void delete(Long userId) {
        Trainer trainer = dao.select(userId);
        trainer.setActive(false);
        dao.delete(userId);
    }

    public Trainer select(Long userId) {
        return dao.select(userId);
    }

    private String createUsername(String firstName, String lastName) {
        StringBuilder username = new StringBuilder(firstName + "." + lastName);
        int counter = 0;
        while (exists(username.toString())) {
            counter++;
            if (counter == 1) {
                username.append(counter);
                continue;
            }
            username = new StringBuilder(username.substring(0, username.length() - String.valueOf(counter - 1).length()));
            username.append(counter);
        }
        return username.toString();
    }

    private boolean exists(String username) {
        for (Trainer trainer : dao.getTrainerMap().values()) {
            if (trainer.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
