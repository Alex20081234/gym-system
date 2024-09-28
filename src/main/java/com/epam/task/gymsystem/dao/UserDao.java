package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.common.ActivityStatusAlreadyExistsException;
import com.epam.task.gymsystem.common.NoExpectedDataInDatabaseException;
import com.epam.task.gymsystem.common.UserNotFoundException;
import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.TrainingType;
import com.epam.task.gymsystem.domain.User;
import java.time.LocalDate;
import java.util.List;

public interface UserDao<T extends User> {
    void create(T user);
    void changePassword(String username, String newPassword) throws UserNotFoundException;
    void update(String username, T updated) throws UserNotFoundException;
    void changeActivityStatus(String username, boolean newActivityStatus) throws UserNotFoundException, ActivityStatusAlreadyExistsException;
    List<Training> selectTrainings(String username,
                                   LocalDate fromDate,
                                   LocalDate toDate,
                                   String partnerUsername,
                                   TrainingType trainingType) throws UserNotFoundException;
    T select(String username) throws UserNotFoundException;
    List<T> selectAll() throws NoExpectedDataInDatabaseException;
}
