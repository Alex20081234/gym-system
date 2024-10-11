package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.domain.Training;
import com.epam.task.gymsystem.domain.TrainingType;
import java.util.List;

public interface TrainingDao {
    void create(Training training);

    Training select(int trainingId);

    List<Training> selectAll();

    List<TrainingType> selectAllTypes();

    TrainingType selectType(String name);
}
