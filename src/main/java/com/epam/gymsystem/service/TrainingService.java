package com.epam.gymsystem.service;

import com.epam.gymsystem.domain.Training;
import com.epam.gymsystem.domain.TrainingCriteria;
import com.epam.gymsystem.domain.TrainingType;
import java.util.List;
import java.util.Optional;

public interface TrainingService {
    void create(Training training);

    Optional<Training> select(int id);

    List<Training> selectAll();

    List<TrainingType> selectAllTypes();

    Optional<TrainingType> selectType(String name);

    List<Training> selectTrainings(String username, TrainingCriteria criteria);
}
