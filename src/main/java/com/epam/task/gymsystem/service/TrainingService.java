package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.domain.Training;
import java.util.Optional;

public interface TrainingService {
    void create(Training training);
    Optional<Training> select(Long id);
}
