package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.TrainingCriteria;
import com.epam.task.gymsystem.domain.User;
import com.epam.task.gymsystem.dto.UsernameAndPassword;
import java.util.List;

public interface UserService<T extends User> {
    UsernameAndPassword create(T user);

    void changePassword(String username, String newPassword);

    String update(String username, T updates);

    void changeActivityStatus(String username, boolean newActivityStatus);

    List<Training> selectTrainings(String username, TrainingCriteria criteria);

    T select(String username);

    List<T> selectAll();

    void loadDependencies(T user);
}
