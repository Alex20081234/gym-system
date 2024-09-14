package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.domain.Trainee;
import java.util.Map;
import java.util.Optional;

public interface TraineeService {
    void create(Trainee trainee);
    void update(Long id, Trainee trainee);
    void delete(Long id);
    Optional<Trainee> select(Long id);
    Optional<Map<Long, Trainee>> selectAll();
}
