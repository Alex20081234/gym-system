package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.dao.TraineeDao;
import com.epam.task.gymsystem.domain.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {
    private final TraineeDao dao;

    @Autowired
    public TraineeServiceImpl(TraineeDao dao) {
        this.dao = dao;
    }

    @Override
    public void create(Trainee trainee) {
        UserService.setUsernameAndPassword(trainee, selectAll().get());
        dao.create(trainee);
    }

    @Override
    public void update(Long userId, Trainee changes) {
        Optional<Trainee> trainee = dao.select(userId);
        boolean nameChanged = false;
        if (changes.getUserId() != null) {
            trainee.get().setUserId(changes.getUserId());
        }
        if (changes.getAddress() != null) {
            trainee.get().setAddress(changes.getAddress());
        }
        if (changes.getDateOfBirth() != null) {
            trainee.get().setDateOfBirth(changes.getDateOfBirth());
        }
        if (changes.getFirstName() != null) {
            trainee.get().setFirstName(changes.getFirstName());
            nameChanged = true;
        }
        if (changes.getLastName() != null) {
            trainee.get().setLastName(changes.getLastName());
            nameChanged = true;
        }
        if (nameChanged) {
            trainee.get().setUsername(UserService.generateUsername(trainee.get().getFirstName(), trainee.get().getLastName(), selectAll().get()));
        }
        if (changes.getPassword() != null) {
            trainee.get().setPassword(changes.getPassword());
        }
        trainee.get().setActive(changes.isActive());
        dao.update(trainee.get());
    }

    @Override
    public void delete(Long userId) {
        Optional<Trainee> trainee = dao.select(userId);
        trainee.get().setActive(false);
        dao.delete(userId);
    }

    @Override
    public Optional<Trainee> select(Long userId) {
        return dao.select(userId);
    }

    @Override
    public Optional<Map<Long, Trainee>> selectAll() {
        return dao.selectAll();
    }
}
