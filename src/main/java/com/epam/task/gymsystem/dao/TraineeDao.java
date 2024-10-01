package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import java.util.List;
import java.util.Map;

public interface TraineeDao extends UserDao<Trainee> {
    List<Trainer> selectNotAssignedTrainers(Trainee trainee);
    void updateTrainers(Trainee trainee, Map<String, Boolean> trainerUsernames);
    void delete(String username);
}
