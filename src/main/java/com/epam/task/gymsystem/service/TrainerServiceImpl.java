package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.common.UserUtils;
import com.epam.task.gymsystem.dao.TrainerDao;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.TrainingCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {
    private final TrainerDao dao;
    private final UserUtils userUtils;
    private static final String NOT_VALID = "Trainer is not valid";

    @Autowired
    public TrainerServiceImpl(TrainerDao dao, UserUtils userUtils) {
        this.dao = dao;
        this.userUtils = userUtils;
    }

    @Override
    public void create(Trainer trainer) {
        if (trainer == null) {
            throw new IllegalArgumentException(NOT_VALID);
        }
        userUtils.setUsernameAndPassword(trainer, selectAll().stream().map(Trainer::getUsername).toList());
        if (!isValid(trainer)) {
            throw new IllegalArgumentException(NOT_VALID);
        }
        dao.create(trainer);
    }

    @Override
    public void changePassword(String username, String newPassword) {
        Trainer trainer = select(username);
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("New password is not valid");
        }
        dao.changePassword(trainer, newPassword);
    }

    @Override
    public void update(String username, Trainer updates) {
        if (updates == null) {
            throw new IllegalArgumentException(NOT_VALID);
        }
        Trainer trainer = select(username);
        dao.update(trainer, (Trainer) userUtils.mergeUsers(trainer, updates, selectAll().stream().map(Trainer::getUsername).toList()));
    }

    @Override
    public void changeActivityStatus(String username, boolean newActivityStatus) {
        Trainer trainer = select(username);
        if (trainer.getIsActive() == newActivityStatus) {
            throw new IllegalArgumentException("Activity status is already " + newActivityStatus);
        }
        dao.changeActivityStatus(trainer, newActivityStatus);
    }

    @Override
    public List<Training> selectTrainings(String username, TrainingCriteria criteria) {
        return dao.selectTrainings(select(username), criteria);
    }

    @Override
    public Trainer select(String username) {
        return dao.select(username);
    }

    @Override
    public List<Trainer> selectAll() {
        return dao.selectAll();
    }

    private boolean isValid(Trainer trainer) {
        return trainer != null && trainer.getFirstName() != null && trainer.getLastName() != null
                && trainer.getUsername() != null && trainer.getPassword() != null
                && trainer.getIsActive() != null && trainer.getSpecialization() != null;
    }
}
