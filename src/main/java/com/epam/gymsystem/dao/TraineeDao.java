package com.epam.gymsystem.dao;

import com.epam.gymsystem.domain.Trainee;
import com.epam.gymsystem.domain.Trainer;
import java.util.List;
import java.util.Map;

public interface TraineeDao extends UserDao<Trainee> {
    List<Trainer> selectNotAssignedTrainers(Trainee trainee);

    void updateTrainers(Trainee trainee, Map<String, Boolean> trainerUsernames);

    void delete(String username);
}
