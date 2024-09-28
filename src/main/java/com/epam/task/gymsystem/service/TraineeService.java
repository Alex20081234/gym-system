package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.common.NoExpectedDataInDatabaseException;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import java.util.List;
import java.util.Map;

public interface TraineeService extends UserService<Trainee> {
    List<Trainer> selectNotAssignedTrainers(String username) throws UserNotFoundException, NoExpectedDataInDatabaseException;
    void updateTrainers(String username, Map<String, Boolean> trainerUsernames) throws UserNotFoundException;
    void delete(String username) throws UserNotFoundException;
}
