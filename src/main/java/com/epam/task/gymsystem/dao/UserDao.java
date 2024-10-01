package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.TrainingCriteria;
import com.epam.task.gymsystem.domain.User;
import java.util.List;

public interface UserDao<T extends User> {
    void create(T user);
    void changePassword(T user, String newPassword);
    void update(T initial, T updated);
    void changeActivityStatus(T user, boolean newActivityStatus);
    List<Training> selectTrainings(T user, TrainingCriteria criteria);
    T select(String username);
    List<T> selectAll();
}
