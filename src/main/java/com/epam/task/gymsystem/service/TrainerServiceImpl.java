package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.common.ActivityStatusAlreadyExistsException;
import com.epam.task.gymsystem.common.NoExpectedDataInDatabaseException;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.common.UserUtils;
import com.epam.task.gymsystem.dao.TrainerDaoImpl;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {
    private final TrainerDaoImpl dao;

    @Autowired
    public TrainerServiceImpl(TrainerDaoImpl dao) {
        this.dao = dao;
    }

    @Override
    public void create(Trainer trainer) {
        UserUtils.setUsernameAndPassword(trainer);
        dao.create(trainer);
    }

    @Override
    public void changePassword(String username, String newPassword) throws UserNotFoundException {
        dao.changePassword(username, newPassword);
    }

    @Override
    public void update(String username, Trainer updates) throws UserNotFoundException {
        Trainer trainer = select(username);
        dao.update(username, (Trainer) UserUtils.mergeUsers(trainer, updates));
    }

    @Override
    public void changeActivityStatus(String username, boolean newActivityStatus) throws UserNotFoundException, ActivityStatusAlreadyExistsException {
        dao.changeActivityStatus(username, newActivityStatus);
    }

    @Override
    public List<Training> selectTrainings(String username, LocalDate fromDate, LocalDate toDate, String partnerUsername, TrainingType trainingType) throws UserNotFoundException {
        return dao.selectTrainings(username, fromDate, toDate, partnerUsername, trainingType);
    }

    @Override
    public Trainer select(String username) throws UserNotFoundException {
        return dao.select(username);
    }

    @Override
    public List<Trainer> selectAll() throws NoExpectedDataInDatabaseException {
        return dao.selectAll();
    }
}
