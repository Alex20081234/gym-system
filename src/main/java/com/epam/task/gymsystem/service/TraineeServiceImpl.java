package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.common.ActivityStatusAlreadyExistsException;
import com.epam.task.gymsystem.common.NoExpectedDataInDatabaseException;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.common.UserUtils;
import com.epam.task.gymsystem.dao.TraineeDaoImpl;
import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class TraineeServiceImpl implements TraineeService {
    private final TraineeDaoImpl dao;

    @Autowired
    public TraineeServiceImpl(TraineeDaoImpl dao) {
        this.dao = dao;
    }

    @Override
    public List<Trainer> selectNotAssignedTrainers(String username) throws UserNotFoundException, NoExpectedDataInDatabaseException {
        return dao.selectNotAssignedTrainers(username);
    }

    @Override
    public void updateTrainers(String username, Map<String, Boolean> trainerUsernames) throws UserNotFoundException {
        dao.updateTrainers(username, trainerUsernames);
    }

    @Override
    public void delete(String username) throws UserNotFoundException {
        dao.delete(username);
    }

    @Override
    public void create(Trainee user) {
        UserUtils.setUsernameAndPassword(user);
        dao.create(user);
    }

    @Override
    public void changePassword(String username, String newPassword) throws UserNotFoundException {
        dao.changePassword(username, newPassword);
    }

    @Override
    public void update(String username, Trainee updates) throws UserNotFoundException {
        Trainee trainee = select(username);
        dao.update(username, (Trainee) UserUtils.mergeUsers(trainee, updates));
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
    public Trainee select(String username) throws UserNotFoundException {
        return dao.select(username);
    }

    @Override
    public List<Trainee> selectAll() throws NoExpectedDataInDatabaseException {
        return dao.selectAll();
    }
}
