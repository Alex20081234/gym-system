package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.common.NoExpectedDataInDatabaseException;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import java.util.List;
import java.util.Map;

public interface TraineeDao extends UserDao<Trainee> {
    List<Trainer> selectNotAssignedTrainers(String username) throws NoExpectedDataInDatabaseException, UserNotFoundException;
    void updateTrainers(String username, Map<String, Boolean> trainerUsernames) throws UserNotFoundException;
    void delete(String username) throws UserNotFoundException;
}
