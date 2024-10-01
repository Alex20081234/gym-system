package com.epam.task.gymsystem.dao;

import com.epam.task.gymsystem.domain.Training;
import java.util.List;

public interface TrainingDao {
    void create(Training training);
    Training select(int trainingId);
    List<Training> selectAll();
}
