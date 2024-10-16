package com.epam.gymsystem.dao;

import com.epam.gymsystem.domain.Training;
import com.epam.gymsystem.domain.TrainingCriteria;
import com.epam.gymsystem.domain.TrainingType;
import java.util.List;

public interface TrainingDao {
    void create(Training training);

    Training select(int trainingId);

    List<Training> selectAll();

    List<TrainingType> selectAllTypes();

    TrainingType selectType(String name);

    List<Training> selectTrainings(String username, TrainingCriteria criteria);
}
