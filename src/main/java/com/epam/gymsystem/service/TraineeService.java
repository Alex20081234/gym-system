package com.epam.gymsystem.service;

import com.epam.gymsystem.domain.Trainee;
import com.epam.gymsystem.domain.Trainer;
import java.util.List;
import java.util.Map;

public interface TraineeService extends UserService<Trainee> {
    List<Trainer> selectNotAssignedTrainers(String username);

    void updateTrainers(String username, Map<String, Boolean> trainerUsernames);

    void delete(String username);
}
