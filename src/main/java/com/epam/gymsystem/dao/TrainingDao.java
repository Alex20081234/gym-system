package com.epam.gymsystem.dao;

import com.epam.gymsystem.domain.Training;
import com.epam.gymsystem.domain.TrainingCriteria;
import java.util.List;
import java.util.Optional;

public interface TrainingDao {
    void create(Training training);

    Optional<Training> select(int trainingId);

    List<Training> selectAll();

    List<Training> selectTrainings(String username, TrainingCriteria criteria);
}
