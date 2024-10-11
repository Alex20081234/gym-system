package com.epam.task.gymsystem.service;

import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.TrainingType;
import java.util.List;

public interface TrainingService {
    void create(Training training);

    Training select(int id);

    List<Training> selectAll();

    List<TrainingType> selectAllTypes();

    TrainingType selectType(String name);
}
