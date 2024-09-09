package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.DAO.TraineeDAO;
import com.epam.task.gymsystem.domain.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraineeService {

    private final TraineeDAO dao;

    @Autowired
    public TraineeService(TraineeDAO dao) {
        this.dao = dao;
    }

    public void create(Trainee trainee) {
        trainee.setUsername(createUsername(trainee.getFirstName(), trainee.getLastName()));
        dao.create(trainee);
    }

    public void update(Long userId, Trainee changes) {
        Trainee trainee = dao.select(userId);
        boolean nameChanged = false;
        if (changes.getUserId() != null) {
            trainee.setUserId(changes.getUserId());
        }
        if (changes.getAddress() != null) {
            trainee.setAddress(changes.getAddress());
        }
        if (changes.getDateOfBirth() != null) {
            trainee.setDateOfBirth(changes.getDateOfBirth());
        }
        if (changes.getFirstName() != null) {
            trainee.setFirstName(changes.getFirstName());
            nameChanged = true;
        }
        if (changes.getLastName() != null) {
            trainee.setLastName(changes.getLastName());
            nameChanged = true;
        }
        if (nameChanged) {
            trainee.setUsername(createUsername(trainee.getFirstName(), trainee.getLastName()));
        }
        if (changes.getUsername() != null) {
            trainee.setUsername(changes.getUsername());
        }
        if (changes.getPassword() != null) {
            trainee.setPassword(changes.getPassword());
        }
        trainee.setActive(changes.isActive());
        dao.update(trainee);
    }

    public void delete(Long userId) {
        Trainee trainee = dao.select(userId);
        trainee.setActive(false);
        dao.delete(userId);
    }

    public Trainee select(Long userId) {
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
        for (Trainee trainee : dao.getTraineeMap().values()) {
            if (trainee.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
