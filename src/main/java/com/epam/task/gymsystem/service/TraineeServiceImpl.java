package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.common.UserUtils;
import com.epam.task.gymsystem.dao.TraineeDao;
import com.epam.task.gymsystem.domain.*;
import com.epam.task.gymsystem.dto.UsernameAndPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final String NOT_VALID = "Trainee is not valid";
    private final TraineeDao dao;

    @Autowired
    public TraineeServiceImpl(TraineeDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> selectNotAssignedTrainers(String username) {
        Trainee trainee = dao.select(username);
        return dao.selectNotAssignedTrainers(trainee);
    }

    @Override
    @Transactional
    public void updateTrainers(String username, Map<String, Boolean> trainerUsernames) {
        if (trainerUsernames == null) {
            throw new IllegalArgumentException("Trainers are not valid");
        }
        Trainee trainee = dao.select(username);
        dao.updateTrainers(trainee, trainerUsernames);
    }

    @Override
    @Transactional
    public void delete(String username) {
        dao.delete(username);
    }

    @Override
    @Transactional
    public UsernameAndPassword create(Trainee trainee) {
        if (trainee == null) {
            throw new IllegalArgumentException(NOT_VALID);
        }
        UsernameAndPassword usernameAndPassword = UserUtils.setUsernameAndPassword(trainee, dao.selectUsernames());
        if (!isValid(trainee)) {
            throw new IllegalArgumentException(NOT_VALID);
        }
        dao.create(trainee);
        return usernameAndPassword;
    }

    @Override
    @Transactional
    public void changePassword(String username, String newPassword) {
        if (newPassword == null || newPassword.length() < 8 || newPassword.length() > 20) {
            throw new IllegalArgumentException("New password is not valid");
        }
        dao.changePassword(username, newPassword);
    }

    @Override
    @Transactional
    public String update(String username, Trainee updates) {
        if (updates == null) {
            throw new IllegalArgumentException(NOT_VALID);
        }
        Trainee trainee = dao.select(username);
        return dao.update(username, (Trainee) UserUtils.mergeUsers(trainee, updates, dao.selectUsernames()));
    }

    @Override
    @Transactional
    public void changeActivityStatus(String username, boolean newActivityStatus) {
        Trainee trainee = dao.select(username);
        if (trainee.getIsActive() == newActivityStatus) {
            throw new IllegalArgumentException("Activity status is already " + newActivityStatus);
        }
        dao.changeActivityStatus(username, newActivityStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> selectTrainings(String username, TrainingCriteria criteria) {
        return dao.selectTrainings(dao.select(username), criteria);
    }

    @Override
    @Transactional(readOnly = true)
    public Trainee select(String username) {
        return dao.select(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> selectAll() {
        return dao.selectAll();
    }

    @Override
    @Transactional(readOnly = true)
    public void loadDependencies(Trainee trainee) {
        dao.loadDependencies(trainee);
    }

    private boolean isValid(Trainee trainee) {
        return trainee != null && trainee.getUsername() != null && trainee.getPassword() != null
                && trainee.getFirstName() != null && trainee.getLastName() != null
                && trainee.getIsActive() != null;
    }
}
