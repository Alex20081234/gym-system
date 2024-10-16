package com.epam.gymsystem.service;

import com.epam.gymsystem.domain.Training;
import com.epam.gymsystem.domain.TrainingCriteria;
import com.epam.gymsystem.domain.TrainingType;
import java.util.List;

public interface TrainingService {
    void create(Training training);

    Training select(int id);

    List<Training> selectAll();

    List<TrainingType> selectAllTypes();

    TrainingType selectType(String name);

    List<Training> selectTrainings(String username, TrainingCriteria criteria);
}
