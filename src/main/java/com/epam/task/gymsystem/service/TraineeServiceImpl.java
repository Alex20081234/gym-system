package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.common.UserUtils;
import com.epam.task.gymsystem.dao.TraineeDao;
import com.epam.task.gymsystem.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class TraineeServiceImpl implements TraineeService {
    private final TraineeDao dao;
    private final UserUtils userUtils;
    private static final String NOT_VALID = "Trainee is not valid";

    @Autowired
    public TraineeServiceImpl(TraineeDao dao, UserUtils userUtils) {
        this.dao = dao;
        this.userUtils = userUtils;
    }

    @Override
    public List<Trainer> selectNotAssignedTrainers(String username) {
        return dao.selectNotAssignedTrainers(select(username));
    }

    @Override
    public void updateTrainers(String username, Map<String, Boolean> trainerUsernames) {
        if (trainerUsernames == null) {
            throw new IllegalArgumentException("Trainers are not valid");
        }
        dao.updateTrainers(select(username), trainerUsernames);
    }

    @Override
    public void delete(String username) {
        dao.delete(username);
    }

    @Override
    public void create(Trainee trainee) {
        if (trainee == null) {
            throw new IllegalArgumentException(NOT_VALID);
        }
        userUtils.setUsernameAndPassword(trainee, selectAll().stream().map(Trainee::getUsername).toList());
        if (!isValid(trainee)) {
            throw new IllegalArgumentException(NOT_VALID);
        }
        dao.create(trainee);
    }

    @Override
    public void changePassword(String username, String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("New password is not valid");
        }
        dao.changePassword(select(username), newPassword);
    }

    @Override
    public void update(String username, Trainee updates) {
        if (updates == null) {
            throw new IllegalArgumentException(NOT_VALID);
        }
        Trainee trainee = select(username);
        dao.update(trainee, (Trainee) userUtils.mergeUsers(trainee, updates, selectAll().stream().map(Trainee::getUsername).toList()));
    }

    @Override
    public void changeActivityStatus(String username, boolean newActivityStatus) {
        Trainee trainee = select(username);
        if (trainee.getIsActive() == newActivityStatus) {
            throw new IllegalArgumentException("Activity status is already " + newActivityStatus);
        }
        dao.changeActivityStatus(trainee, newActivityStatus);
    }

    @Override
    public List<Training> selectTrainings(String username, TrainingCriteria criteria) {
        return dao.selectTrainings(select(username), criteria);
    }

    @Override
    public Trainee select(String username) {
        return dao.select(username);
    }

    @Override
    public List<Trainee> selectAll() {
        return dao.selectAll();
    }

    private boolean isValid(Trainee trainee) {
        return trainee != null && trainee.getUsername() != null && trainee.getPassword() != null
                && trainee.getFirstName() != null && trainee.getLastName() != null
                && trainee.getIsActive() != null && trainee.getDateOfBirth() != null
                && trainee.getAddress() != null;
    }
}
