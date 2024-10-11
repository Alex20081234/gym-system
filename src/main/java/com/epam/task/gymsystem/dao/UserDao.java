package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.TrainingCriteria;
import com.epam.task.gymsystem.domain.User;
import java.util.List;

public interface UserDao<T extends User> {
    void create(T user);

    void changePassword(String username, String newPassword);

    void update(String username, T updated);

    void changeActivityStatus(String username, boolean newActivityStatus);

    List<Training> selectTrainings(T user, TrainingCriteria criteria);

    T select(String username);

    List<T> selectAll();

    List<String> selectUsernames();
}
