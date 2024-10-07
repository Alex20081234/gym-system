package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.TrainingCriteria;
import com.epam.task.gymsystem.domain.User;
import java.util.List;

public interface UserService<T extends User> {
    void create(T user);

    void changePassword(String username, String newPassword);

    void update(String username, T updates);

    void changeActivityStatus(String username, boolean newActivityStatus);

    List<Training> selectTrainings(String username, TrainingCriteria criteria);

    T select(String username);

    List<T> selectAll();
}
