package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.domain.Trainee;
import com.epam.task.gymsystem.domain.Trainer;
import java.util.List;
import java.util.Map;

public interface TraineeService extends UserService<Trainee> {
    List<Trainer> selectNotAssignedTrainers(String username);
    void updateTrainers(String username, Map<String, Boolean> trainerUsernames);
    void delete(String username);
}
